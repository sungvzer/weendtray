package it.garganovolpe.weendtray.telephone.billing;

public class Wallet {
    private final int id;
    private final double balance;
    private final int userId;
    private int messagesCount;
    private int minutesCount;
    private double dataCount;

    public Wallet(int id, double balance, int userId) {
        this.id = id;
        this.balance = balance;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public int getUserId() {
        return userId;
    }

    public int getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(int messagesCount) {
        if (messagesCount < 0) {
            messagesCount = 0;
        }
        this.messagesCount = messagesCount;
    }

    public int getMinutesCount() {
        return minutesCount;
    }

    public void setMinutesCount(int minutesCount) {
        if (minutesCount < 0) {
            minutesCount = 0;
        }
        this.minutesCount = minutesCount;
    }

    public double getDataCount() {
        return dataCount;
    }

    public void setDataCount(double dataCount) {
        if (dataCount < 0) {
            dataCount = 0;
        }
        this.dataCount = dataCount;
    }

}
