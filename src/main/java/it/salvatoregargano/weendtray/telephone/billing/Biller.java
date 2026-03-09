package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.acl.PhonePlan;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.patterns.Observer;
import it.salvatoregargano.weendtray.telephone.CallEvent;
import it.salvatoregargano.weendtray.telephone.DataUsageEvent;
import it.salvatoregargano.weendtray.telephone.MessageEvent;
import it.salvatoregargano.weendtray.telephone.PhoneEvent;

public class Biller implements Observer<PhoneEvent> {
    private double messageCost(MessageEvent event) {
        if (event.getPlan() == PhonePlan.BUSINESS) {
            return 0;
        } else if (event.getPlan() == PhonePlan.PREMIUM) {
            return 0.0075 * event.getContent().length();
        } else if (event.getPlan() == PhonePlan.REGULAR) {
            return 0.01 * event.getContent().length();
        }

        return 0;
    }

    private double callCost(CallEvent event) {
        if (event.getPlan() == PhonePlan.BUSINESS) {
            return 0;
        } else if (event.getPlan() == PhonePlan.PREMIUM) {
            return 0.05 * event.getDuration().toMinutes();
        } else if (event.getPlan() == PhonePlan.REGULAR) {
            return 0.1 * event.getDuration().toMinutes();
        }

        return 0;
    }

    private double dataCost(DataUsageEvent event) {
        if (event.getPlan() == PhonePlan.BUSINESS) {
            return 0;
        } else if (event.getPlan() == PhonePlan.PREMIUM) {
            return 0.00025 * event.getDataSizeKB();
        } else if (event.getPlan() == PhonePlan.REGULAR) {
            return 0.005 * event.getDataSizeKB();
        }
        return 0;
    }

    @Override
    public void update(PhoneEvent event) {
        final var logger = CombinedLogger.getInstance();

        double billableCost = 0;
        final var user = UserPersistence.getUserByPhoneNumber(event.getSourceNumber());
        if (user == null) {
            logger.error("Could not bill non-existent user with phone number " + event.getSourceNumber());
            return;
        }

        if (event instanceof MessageEvent messageEvent) {
            billableCost = messageCost(messageEvent);
        } else if (event instanceof CallEvent callEvent) {
            billableCost = callCost(callEvent);
        } else if (event instanceof DataUsageEvent dataUsage) {
            billableCost = dataCost(dataUsage);
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
