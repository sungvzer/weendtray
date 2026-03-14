package it.garganovolpe.weendtray.telephone.billing;
/* 
 * A builder for creating instances of the {@link Wallet} class.
 * This builder allows for a fluent interface to set the properties of the wallet before building it.
 */
public class WalletBuilder {
    private int id;
    private double balance;
    private int userId;
    private int messagesCount;
    private int minutesCount;
    private double dataCount;

    public WalletBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public WalletBuilder withBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public WalletBuilder withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public WalletBuilder withMessagesCount(int messagesCount) {
        this.messagesCount = messagesCount;
        return this;
    }

    public WalletBuilder withMinutesCount(int minutesCount) {
        this.minutesCount = minutesCount;
        return this;
    }

    public WalletBuilder withDataCount(double dataCount) {
        this.dataCount = dataCount;
        return this;
    }

    public Wallet build() {
        Wallet wallet = new Wallet(id, balance, userId);
        wallet.setMessagesCount(messagesCount);
        wallet.setMinutesCount(minutesCount);
        wallet.setDataCount(dataCount);
        return wallet;
    }
}
