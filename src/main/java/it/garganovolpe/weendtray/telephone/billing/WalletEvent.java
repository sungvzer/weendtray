package it.garganovolpe.weendtray.telephone.billing;

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
