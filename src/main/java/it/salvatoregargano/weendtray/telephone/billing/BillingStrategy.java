package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.telephone.CallEvent;
import it.salvatoregargano.weendtray.telephone.DataUsageEvent;
import it.salvatoregargano.weendtray.telephone.MessageEvent;

public interface BillingStrategy {
    double calculateMessageCost(MessageEvent event);

    double calculateCallCost(CallEvent event);

    double calculateDataCost(DataUsageEvent event);
}
