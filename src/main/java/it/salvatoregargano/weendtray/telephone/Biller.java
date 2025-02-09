package it.salvatoregargano.weendtray.telephone;

import it.salvatoregargano.weendtray.patterns.Observer;

public class Biller implements Observer<PhoneEvent> {
    private double messageCost(MessageEvent event) {
        final double costPerCharacter = switch (event.getPlan()) {
            case REGULAR -> 0.01;
            case PREMIUM -> 0.0075;
            case BUSINESS -> 0.005;
        };

        double cost = costPerCharacter * event.getContent().length();
        return cost;
    }

    private double callCost(CallEvent event) {
        final double costPerMinute = switch (event.getPlan()) {
            case REGULAR -> 0.1;
            case PREMIUM -> 0.05;
            case BUSINESS -> 0.0;
        };

        double cost = costPerMinute * event.getDuration().toMinutes();
        return cost;
    }

    private double dataCost(DataUsageEvent event) {
        final double costPerMB = switch (event.getPlan()) {
            case REGULAR -> 0.1;
            case PREMIUM -> 0.05;
            case BUSINESS -> 0.0;
        };

        double cost = costPerMB * event.getDataSizeMB();
        return cost;
    }

    @Override
    public void update(PhoneEvent event) {
        double billableCost = 0;
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
