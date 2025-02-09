package it.salvatoregargano.weendtray.telephone.billing;

public class Wallet {
    private final int id;
    private final double balance;
    private final int userId;

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
}
