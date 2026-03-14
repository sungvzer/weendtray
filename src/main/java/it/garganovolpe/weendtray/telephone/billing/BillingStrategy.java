package it.garganovolpe.weendtray.telephone.billing;
/*
    * An interface defining the strategy for calculating costs of messages, calls, and data usage.
    * This allows for different billing strategies to be implemented and used interchangeably.
    * @see RegularBillingStrategy
    * @see BusinessBillingStrategy
    * @see PremiumBillingStrategy
*/
import it.garganovolpe.weendtray.telephone.CallEvent;
import it.garganovolpe.weendtray.telephone.DataUsageEvent;
import it.garganovolpe.weendtray.telephone.MessageEvent;

public interface BillingStrategy {
    double calculateMessageCost(MessageEvent event);

    double calculateCallCost(CallEvent event);

    double calculateDataCost(DataUsageEvent event);
}
