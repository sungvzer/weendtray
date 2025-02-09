package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.persistence.DatabaseConnection;

import java.sql.SQLException;

public class WalletService {
    private static WalletService instance;

    private WalletService() {
    }

    public static WalletService getInstance() {
        if (instance == null) {
            instance = new WalletService();
        }
        return instance;
    }

    public Wallet getWallet(int userId) throws SQLException {
        var statement = DatabaseConnection.getInstance().getConnection().createStatement();
        var resultSet = statement.executeQuery("SELECT * FROM wallet WHERE user_id = " + userId);
        if (resultSet.next()) {
            return new Wallet(
                    resultSet.getInt("id"),
                    resultSet.getDouble("balance"),
                    resultSet.getInt("user_id")
            );
        } else {
            // Create a new wallet for the user
            var createStatement = DatabaseConnection.getInstance().getConnection().prepareStatement("INSERT INTO wallet (balance, user_id) VALUES (0, ?) RETURNING id, balance, user_id");
            createStatement.setInt(1, userId);
            resultSet = createStatement.executeQuery();
            if (resultSet.next()) {
                return new Wallet(
                        resultSet.getInt("id"),
                        resultSet.getDouble("balance"),
                        resultSet.getInt("user_id")
                );
            }
        }
        return null;
    }

    public void addAmountToWallet(Wallet wallet, double amount) throws SQLException {
        final var logger = CombinedLogger.getInstance();
        if (wallet == null) {
            logger.error("Tried to charge a null wallet");
            return;
        }
        var statement = DatabaseConnection.getInstance().getConnection().createStatement();
        var resultSet = statement.executeQuery("SELECT * FROM wallet WHERE id = " + wallet.getId());
        if (!resultSet.next()) {
            logger.error("Tried to charge a wallet that does not exist");
        }

        var newBalance = wallet.getBalance() + amount;
        statement.executeUpdate("UPDATE wallet SET balance = " + newBalance + " WHERE id = " + wallet.getId());
    }
}
