package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.patterns.Observer;

import java.sql.SQLException;

public class WalletEventHandler implements Observer<WalletEvent> {
    @Override
    public void update(WalletEvent event) {
        final var logger = CombinedLogger.getInstance();
        final var wallet = event.wallet();
        final var amount = event.amount();

        try {
            WalletService.getInstance().addAmountToWallet(wallet, amount);
        } catch (SQLException e) {
            logger.error("Failed handle event for wallet " + wallet.getId());
        }
    }
}
