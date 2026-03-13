package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.logging.GetLoggerProviderFromEnv;
import it.salvatoregargano.weendtray.logging.LoggerInjector;
import it.salvatoregargano.weendtray.logging.LoggerProvider;
import it.salvatoregargano.weendtray.patterns.Observer;
import it.salvatoregargano.weendtray.telephone.CallEvent;
import it.salvatoregargano.weendtray.telephone.DataUsageEvent;
import it.salvatoregargano.weendtray.telephone.MessageEvent;
import it.salvatoregargano.weendtray.telephone.PhoneEvent;

/**
 * The Biller class is responsible for handling billing operations related to
 * phone events. It implements the Observer interface, allowing it to receive
 * updates when phone events occur. When a phone event is received, the Biller
 * calculates the cost associated with the event based on the user's phone plan
 * and billing strategy. It then charges the user's wallet accordingly. The
 * Biller interacts with the UserPersistence to retrieve user information and
 * with the WalletService to manage wallet transactions. Additionally, it uses
 * the CombinedLogger to log any errors or important information during the
 * billing process.
 */
public class Biller implements Observer<PhoneEvent> {
    private static Biller instance;
    @GetLoggerProviderFromEnv(defaultType = "COMBINED")
    private LoggerProvider loggerProvider;

    private Biller() {
        LoggerInjector.inject(this);
    }

    public static Biller getInstance() {
        if (instance == null) {
            instance = new Biller();
        }
        return instance;
    }

    /**
     * Handles the update of a phone event by calculating the billable cost based on
     * the user's phone plan and billing strategy, and then charging the user's
     * wallet accordingly.
     */
    @Override
    public void update(PhoneEvent event) {
        final var logger = loggerProvider.createLogger();
        double billableCost = 0;
        final var user = UserPersistence.getInstance().getUserByPhoneNumber(event.getSourceNumber());
        if (user == null) {
            logger.error("Could not bill non-existent user with phone number " + event.getSourceNumber());
            return;
        }
        Wallet userWallet = null;

        try {
            userWallet = WalletService.getInstance().getWallet(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final var phonePlan = user.getPhonePlan();
        final var billingStrategy = phonePlan.getBillingStrategy();
        final var accountKind = user.getKind();
        if (accountKind == null) {
            logger.error("User " + user.getId() + " has no account kind, cannot bill");
            return;
        }

        if (event instanceof MessageEvent messageEvent) {
            if (accountKind.equals(UserAccountKind.PAY_AS_YOU_GO)) {
                billableCost = billingStrategy.calculateMessageCost(messageEvent);
            }
        } else if (event instanceof CallEvent callEvent) {
            if (accountKind.equals(UserAccountKind.PAY_AS_YOU_GO)) {
                billableCost = billingStrategy.calculateCallCost(callEvent);
            }
        } else if (event instanceof DataUsageEvent dataUsage) {

            if (accountKind.equals(UserAccountKind.PAY_AS_YOU_GO)) {
                billableCost = billingStrategy.calculateDataCost(dataUsage);
            }
        }

        try {
            WalletService.getInstance().addAmountToWallet(userWallet, -billableCost);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (billableCost > 0) {
            logger.info("Billed user " + user.getId() + " for event " + event.getDescription() + " with cost "
                    + billableCost);
        }

        if (accountKind.equals(UserAccountKind.FIXED_COST)) {
            if (event instanceof MessageEvent messageEvent) {
                try {
                    int messageLength = messageEvent.getContent().length();
                    double messageCost = billingStrategy.calculateMessageCost(messageEvent);
                    if (userWallet.getMessagesCount() <= 0) {
                        logger.warn("User " + user.getId() + " has no messages left, but sent a message of length "
                                + messageLength + ". Charging for the message.");
                        WalletService.getInstance().addAmountToWallet(userWallet, -messageCost);
                        return;
                    }
                    int messagesToCharge = messageLength > 160 ? 2 : 1;
                    if (userWallet.getMessagesCount() < messagesToCharge) {
                        int remaining = messagesToCharge - userWallet.getMessagesCount();
                        logger.warn("User " + user.getId() + " has only " + userWallet.getMessagesCount()
                                + " messages left, but sent a message of length " + messageLength
                                + ". Charging for the remaining " + remaining + " messages.");
                        WalletService.getInstance().addAmountToWallet(userWallet, -messageCost * remaining);
                        WalletService.getInstance().addMessages(userWallet, -userWallet.getMessagesCount());
                        return;
                    }

                    WalletService.getInstance().addMessages(userWallet, -messagesToCharge);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (event instanceof CallEvent callEvent) {
                try {
                    int minutesToCharge = (int) callEvent.getDuration().toSeconds() / 60;
                    if (userWallet.getMinutesCount() <= 0) {
                        logger.warn("User " + user.getId() + " has no minutes left, but made a call of duration "
                                + callEvent.getDuration() + ". Charging for the call.");
                        WalletService.getInstance().addAmountToWallet(userWallet,
                                -billingStrategy.calculateCallCost(callEvent));
                        return;
                    }
                    if (userWallet.getMinutesCount() < minutesToCharge) {
                        int remaining = minutesToCharge - userWallet.getMinutesCount();
                        logger.warn("User " + user.getId() + " has only " + userWallet.getMinutesCount()
                                + " minutes left, but made a call of duration " + callEvent.getDuration()
                                + ". Charging for the remaining " + remaining + " minutes.");
                        WalletService.getInstance().addAmountToWallet(userWallet,
                                -billingStrategy.calculateCallCost(callEvent));
                        WalletService.getInstance().addMinutes(userWallet, -userWallet.getMinutesCount());
                        return;
                    }

                    WalletService.getInstance().addMinutes(userWallet, -minutesToCharge);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (event instanceof DataUsageEvent dataUsage) {
                try {
                    double dataToCharge = dataUsage.getDataSizeKB() / 1024.0;
                    if (userWallet.getDataCount() <= 0) {
                        logger.warn("User " + user.getId() + " has no data left, but used " + dataToCharge
                                + " MB. Charging for the data.");
                        WalletService.getInstance().addAmountToWallet(userWallet,
                                -billingStrategy.calculateDataCost(dataUsage));
                        return;
                    }
                    if (userWallet.getDataCount() < dataToCharge) {
                        double remaining = dataToCharge - userWallet.getDataCount();
                        logger.warn("User " + user.getId() + " has only " + userWallet.getDataCount()
                                + " MB left, but used " + dataToCharge + " MB. Charging for the remaining " + remaining
                                + " MB.");
                        WalletService.getInstance().addAmountToWallet(userWallet,
                                -billingStrategy.calculateDataCost(dataUsage));
                        WalletService.getInstance().addData(userWallet, -userWallet.getDataCount());
                        return;
                    }

                    WalletService.getInstance().addData(userWallet, -dataToCharge);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
