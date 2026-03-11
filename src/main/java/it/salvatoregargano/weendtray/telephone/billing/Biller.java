package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
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

    /**
     * Handles the update of a phone event by calculating the billable cost based on
     * the user's phone plan and billing strategy, and then charging the user's
     * wallet accordingly.
     */
    @Override
    public void update(PhoneEvent event) {
        final var logger = CombinedLogger.getInstance();

        double billableCost = 0;
        final var user = UserPersistence.getUserByPhoneNumber(event.getSourceNumber());
        if (user == null) {
            logger.error("Could not bill non-existent user with phone number " + event.getSourceNumber());
            return;
        }

        final var phonePlan = user.getPhonePlan();
        final var billingStrategy = phonePlan.getBillingStrategy();

        if (event instanceof MessageEvent messageEvent) {
            billableCost = billingStrategy.calculateMessageCost(messageEvent);
        } else if (event instanceof CallEvent callEvent) {
            billableCost = billingStrategy.calculateCallCost(callEvent);
        } else if (event instanceof DataUsageEvent dataUsage) {
            billableCost = billingStrategy.calculateDataCost(dataUsage);
        }

        // Charge the wallet
        final var walletService = WalletService.getInstance();
        try {
            final var wallet = walletService.getWallet(user.getId());
            walletService.addAmountToWallet(wallet, -billableCost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
