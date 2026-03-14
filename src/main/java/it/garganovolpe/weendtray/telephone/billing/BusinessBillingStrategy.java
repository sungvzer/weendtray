package it.garganovolpe.weendtray.telephone.billing;
/*
    * Implementation of the {@link BillingStrategy} interface for business customers.
 */
import it.garganovolpe.weendtray.telephone.CallEvent;
import it.garganovolpe.weendtray.telephone.DataUsageEvent;
import it.garganovolpe.weendtray.telephone.MessageEvent;

public class BusinessBillingStrategy implements BillingStrategy {
    @Override
    public double calculateMessageCost(MessageEvent event) {
        return 0;
    }

    @Override
    public double calculateCallCost(CallEvent event) {
        return 0;
    }

    @Override
    public double calculateDataCost(DataUsageEvent event) {
        return 0;
    }
}
