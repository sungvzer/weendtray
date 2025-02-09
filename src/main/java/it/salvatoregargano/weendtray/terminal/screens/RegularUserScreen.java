package it.salvatoregargano.weendtray.terminal.screens;

import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.telephone.PhoneActivity;
import it.salvatoregargano.weendtray.telephone.PhoneEventLogger;
import it.salvatoregargano.weendtray.telephone.billing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;

public class RegularUserScreen extends UserScreen<RegularUserScreen.RegularUserCommand> {
    private final PhoneActivity phone;
    private final WalletActivity wallet;
    private final RegularUser user;

    public RegularUserScreen(RegularUser user) {
        super(user);
        this.user = user;
        this.phone = new PhoneActivity(user.getPhoneNumber(), user.getPhonePlan());
        this.wallet = new WalletActivity();

        phone.addObserver(new Biller());
        phone.addObserver(new PhoneEventLogger());

        wallet.addObserver(new WalletEventHandler());
    }

    private void call() throws IOException {
        String targetNumber;
        int duration;
        final var rb = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Register a call event.");
        System.out.print("Target number: ");
        targetNumber = rb.readLine();

        if (targetNumber.isBlank()) {
            System.out.println("Invalid target number.");
            return;
        }

        if (targetNumber.equals(user.getPhoneNumber())) {
            System.out.println("You cannot call yourself.");
            return;
        }

        System.out.print("Duration (minutes): ");
        duration = Integer.parseInt(rb.readLine());

        if (duration <= 0) {
            System.out.println("Invalid duration.");
            return;
        }

        final var foundUser = UserPersistence.getUserByPhoneNumber(targetNumber);
        if (foundUser == null) {
            System.out.println("User not found.");
            return;
        }

        phone.makeCall(targetNumber, Duration.ofMinutes(duration));
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("/logout - Log out.");
        System.out.println("/exit - Exit the application.");
        System.out.println("/help - Show this help message.");
        System.out.println("/info - Show user information.");
        System.out.println("/money - Show wallet information.");
        System.out.println("/call - Make a call.");
        System.out.println("/sms - Send a message.");
        System.out.println("/internet - Use mobile data.");
        System.out.println("/top_up - Top up your wallet.");
    }

    @Override
    public boolean show() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;

        System.out.println("Welcome, " + user.getUsername() + "!");
        System.out.println("You are a regular user.");

        while (running) {
            System.out.print("user > ");
            var input = br.readLine();
            var command = parseCommand(input);
            if (command == null) {
                System.out.println("Invalid command.");
                continue;
            }

            switch (command) {
                case LOGOUT:
                    running = false;
                    break;
                case HELP:
                    showHelp();
                    break;
                case INFO:
                    showInfo();
                    break;
                case EXIT:
                    return false;
                case CALL:
                    call();
                    break;
                case SMS:
                    sms();
                    break;
                case INTERNET:
                    dataUsage();
                    break;
                case TOP_UP:
                    topUp();
                    break;
                case MONEY:
                    money();
                    break;
            }
        }

        return true;
    }

    private void money() throws SQLException {
        final var userWallet = WalletService.getInstance().getWallet(user.getId());
        System.out.println("Wallet balance: " + userWallet.getBalance());
    }

    private void topUp() throws SQLException {
        final var rb = new BufferedReader(new InputStreamReader(System.in));
        int amount;
        PaymentMethod paymentMethod;

        System.out.println("Top up your wallet.");
        System.out.print("Amount: ");
        try {
            amount = Integer.parseInt(rb.readLine());
        } catch (NumberFormatException | IOException e) {
            System.out.println("Invalid amount.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }

        System.out.println("Select a payment method:");
        System.out.println("1. Credit card");
        System.out.println("2. Cash");
        System.out.println("3. Bancomat");
        System.out.print("Choice: ");
        try {
            int choice = Integer.parseInt(rb.readLine());
            switch (choice) {
                case 1:
                    paymentMethod = PaymentMethod.CREDIT_CARD;
                    break;
                case 2:
                    paymentMethod = PaymentMethod.CASH;
                    break;
                case 3:
                    paymentMethod = PaymentMethod.BANCOMAT;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println("Invalid choice.");
            return;
        }


        final var userWallet = WalletService.getInstance().getWallet(user.getId());

        wallet.addAmountToWallet(userWallet, amount, paymentMethod);
    }

    private void dataUsage() throws IOException {
        final var rb = new BufferedReader(new InputStreamReader(System.in));

        String urlToRead;
        int data;

        System.out.println("Register a data usage event.");
        System.out.print("URL: ");
        urlToRead = rb.readLine();

        if (urlToRead.isBlank()) {
            System.out.println("Invalid URL.");
            return;
        }

        // find out real data usage by calling a GET request to the URL
        StringBuilder result = new StringBuilder();
        URL url;
        try {
            url = new URI(urlToRead).toURL();
        } catch (IllegalArgumentException | URISyntaxException e) {
            System.out.println("Invalid URL.");
            return;
        }

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        data = result.toString().length();

        phone.useData(data);
    }

    private void sms() throws IOException {
        final var rb = new BufferedReader(new InputStreamReader(System.in));
        String targetNumber;
        String content;

        System.out.println("Register a message event.");
        System.out.print("Target number: ");
        targetNumber = rb.readLine();
        if (targetNumber.isBlank()) {
            System.out.println("Invalid target number.");
            return;
        }

        final var foundUser = UserPersistence.getUserByPhoneNumber(targetNumber);
        if (foundUser == null) {
            System.out.println("User not found.");
            return;
        }
        if (targetNumber.equals(user.getPhoneNumber())) {
            System.out.println("You cannot send a message to yourself.");
            return;
        }

        System.out.print("Content: ");
        content = rb.readLine().trim();

        if (content.isBlank()) {
            System.out.println("Invalid content.");
            return;
        }


        phone.sendMessage(targetNumber, content);
    }

    private void showInfo() {
        System.out.println("Full name: " + user.getName() + " " + user.getSurname());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Plan: " + user.getPhonePlan());
        System.out.println("[[ REGULAR USER ]]");
    }

    @Override
    protected RegularUserCommand parseCommand(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim().toLowerCase();
        if (input.isBlank()) {
            return null;
        }

        if (!input.startsWith("/")) {
            return null;
        }

        try {
            return RegularUserCommand.valueOf(input.replaceAll("/", "").toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public enum RegularUserCommand {
        LOGOUT,
        EXIT,
        INFO,
        HELP,
        CALL,
        SMS,
        INTERNET,
        MONEY, TOP_UP
    }
}
