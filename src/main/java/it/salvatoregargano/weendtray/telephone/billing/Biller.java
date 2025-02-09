package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.patterns.Observer;
import it.salvatoregargano.weendtray.telephone.CallEvent;
import it.salvatoregargano.weendtray.telephone.DataUsageEvent;
import it.salvatoregargano.weendtray.telephone.MessageEvent;
import it.salvatoregargano.weendtray.telephone.PhoneEvent;

public class Biller implements Observer<PhoneEvent> {
    private double messageCost(MessageEvent event) {
        final double costPerCharacter = switch (event.getPlan()) {
            case REGULAR -> 0.01;
            case PREMIUM -> 0.0075;
            case BUSINESS -> 0.005;
        };

        return costPerCharacter * event.getContent().length();
    }

    private double callCost(CallEvent event) {
        final double costPerMinute = switch (event.getPlan()) {
            case REGULAR -> 0.1;
            case PREMIUM -> 0.05;
            case BUSINESS -> 0.0;
        };

        return costPerMinute * event.getDuration().toMinutes();
    }

    private double dataCost(DataUsageEvent event) {
        final double costPerKB = switch (event.getPlan()) {
            case REGULAR -> 0.005;
            case PREMIUM -> 0.00025;
            case BUSINESS -> 0;
        };

        return costPerKB * event.getDataSizeKB();
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
