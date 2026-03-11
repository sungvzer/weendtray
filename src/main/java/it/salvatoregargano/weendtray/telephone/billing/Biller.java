package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.patterns.Observer;
import it.salvatoregargano.weendtray.telephone.CallEvent;
import it.salvatoregargano.weendtray.telephone.DataUsageEvent;
import it.salvatoregargano.weendtray.telephone.MessageEvent;
import it.salvatoregargano.weendtray.telephone.PhoneEvent;

public class Biller implements Observer<PhoneEvent> {
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
