package it.salvatoregargano.weendtray.telephone.billing;

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
        double billableCost;
        // TODO: Implement billing logic
        if (event instanceof MessageEvent messageEvent) {
            billableCost = messageCost(messageEvent);
            System.out.println("[BILLER] SMS Event: " + messageEvent.getUUID() + " - Cost: €" + billableCost);
        } else if (event instanceof CallEvent callEvent) {
            billableCost = callCost(callEvent);
            System.out.println("[BILLER] Call Event: " + callEvent.getUUID() + " - Cost: €" + billableCost);
        } else if (event instanceof DataUsageEvent dataUsage) {
            billableCost = dataCost(dataUsage);
            System.out.println("[BILLER] Data Usage Event: " + dataUsage.getUUID() + " - Cost: €" + billableCost);
        }
    }
}
