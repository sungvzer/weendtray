package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.patterns.Observable;

public class WalletActivity extends Observable<WalletEvent> {
    public void addAmountToWallet(Wallet wallet, double amount, PaymentMethod paymentMethod) {
        notifyObservers(new WalletEvent(wallet, amount, paymentMethod));
    }
}
