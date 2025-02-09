package it.salvatoregargano.weendtray.telephone.billing;

public record WalletEvent(Wallet wallet, double amount, PaymentMethod paymentMethod) {
}
