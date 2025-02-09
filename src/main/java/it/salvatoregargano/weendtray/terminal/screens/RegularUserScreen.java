package it.salvatoregargano.weendtray.terminal.screens;

import it.salvatoregargano.weendtray.acl.RegularUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegularUserScreen extends UserScreen<RegularUserScreen.RegularUserCommand> {
    private RegularUser user;

    public RegularUserScreen(RegularUser user) {
        super(user);
        this.user = user;
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("/logout - Log out.");
        System.out.println("/exit - Exit the application.");
        System.out.println("/help - Show this help message.");
        System.out.println("/info - Show user information.");
    }

    @Override
    public boolean show() throws IOException {
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
            }
        }

        return true;
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
        HELP
    }
}
