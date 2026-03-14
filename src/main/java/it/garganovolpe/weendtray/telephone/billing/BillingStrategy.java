package it.garganovolpe.weendtray.telephone.billing;

import it.garganovolpe.weendtray.telephone.CallEvent;
import it.garganovolpe.weendtray.telephone.DataUsageEvent;
import it.garganovolpe.weendtray.telephone.MessageEvent;

public interface BillingStrategy {
    double calculateMessageCost(MessageEvent event);

    double calculateCallCost(CallEvent event);

    double calculateDataCost(DataUsageEvent event);
}
