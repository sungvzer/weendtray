package it.salvatoregargano.weendtray.telephone.billing;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.persistence.DatabaseConnection;

/**
 * The WalletService class is responsible for managing user wallets, which are
 * used for billing purposes. It provides methods to retrieve a user's wallet
 * and to add or subtract amounts from the wallet balance. The service
 * interacts with the
 * database to persist wallet information.
 */
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
        try (var preparedStatement = DatabaseConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM wallet WHERE user_id = ?")) {
            preparedStatement.setInt(1, userId);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return fromResultSet(resultSet);
            } else {
                // Create a new wallet for the user
                var createStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(
                        "INSERT INTO wallet (balance, user_id) VALUES (0, ?)");
                createStatement.setInt(1, userId);
                createStatement.executeUpdate();
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return fromResultSet(resultSet);
                }
            }
        }

        return null;
    }

    public void addMessages(Wallet wallet, int messages) throws SQLException {
        final var logger = CombinedLogger.getInstance();
        if (wallet == null) {
            logger.error("Tried to add messages to a null wallet");
            return;
        }
        var statement = DatabaseConnection.getInstance().getConnection().createStatement();
        var resultSet = statement.executeQuery("SELECT * FROM wallet WHERE id = " + wallet.getId());
        if (!resultSet.next()) {
            logger.error("Tried to add messages to a wallet that does not exist");
        }

        var newMessagesCount = wallet.getMessagesCount() + messages;
        statement.executeUpdate(
                "UPDATE wallet SET messages_count = " + newMessagesCount + " WHERE id = " + wallet.getId());
    }

    public void addMinutes(Wallet wallet, int minutes) throws SQLException {
        final var logger = CombinedLogger.getInstance();
        if (wallet == null) {
            logger.error("Tried to add minutes to a null wallet");
            return;
        }
        var statement = DatabaseConnection.getInstance().getConnection().createStatement();
        var resultSet = statement.executeQuery("SELECT * FROM wallet WHERE id = " + wallet.getId());
        if (!resultSet.next()) {
            logger.error("Tried to add minutes to a wallet that does not exist");
        }

        var newMinutesCount = wallet.getMinutesCount() + minutes;
        statement.executeUpdate(
                "UPDATE wallet SET minutes_count = " + newMinutesCount + " WHERE id = " + wallet.getId());
    }

    public void addData(Wallet wallet, double dataMB) throws SQLException {
        final var logger = CombinedLogger.getInstance();
        if (wallet == null) {
            logger.error("Tried to add data to a null wallet");
            return;
        }
        var statement = DatabaseConnection.getInstance().getConnection().createStatement();
        var resultSet = statement.executeQuery("SELECT * FROM wallet WHERE id = " + wallet.getId());
        if (!resultSet.next()) {
            logger.error("Tried to add data to a wallet that does not exist");
        }

        var newDataCount = wallet.getDataCount() + dataMB;
        statement.executeUpdate(
                "UPDATE wallet SET data_count = " + newDataCount + " WHERE id = " + wallet.getId());
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

    private Wallet fromResultSet(ResultSet resultSet) throws SQLException {
        return new WalletBuilder()
                .withId(resultSet.getInt("id"))
                .withBalance(resultSet.getDouble("balance"))
                .withUserId(resultSet.getInt("user_id"))
                .withDataCount(resultSet.getDouble("data_count"))
                .withMessagesCount(resultSet.getInt("messages_count"))
                .withMinutesCount(resultSet.getInt("minutes_count"))
                .build();
    }
}
