package it.garganovolpe.weendtray.telephone.billing;
/*
    * A class representing an event related to a user's wallet, such as a recharge or payment.
    * This class encapsulates the wallet involved, the amount of the transaction, and the payment method used.
*/
public class WalletEvent {
    private final Wallet wallet;
    private final double amount;
    private final PaymentMethod paymentMethod;

    public WalletEvent(Wallet wallet, double amount, PaymentMethod paymentMethod) {
        this.wallet = wallet;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
}
