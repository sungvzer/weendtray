package it.salvatoregargano.weendtray.terminal.screens;

import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.acl.UserRole;
import it.salvatoregargano.weendtray.logging.CombinedLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdminUserScreen extends UserScreen<AdminUserScreen.AdminUserCommand> {
    public AdminUserScreen(User user) {
        super(user);
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("/logout - Log out.");
        System.out.println("/exit - Exit the application.");
        System.out.println("/promote - Promote a user to admin.");
        System.out.println("/help - Show this help message.");
    }

    private void promoteUser() {
        var logger = CombinedLogger.getInstance();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the username of the user you want to promote: ");
        try {
            var username = br.readLine();
            var user = UserPersistence.getUserByUsername(username);
            if (user == null) {
                logger.info("Tried to promote non-existing user:" + username);
                System.out.println("User does not exist.");
                return;
            }
            if (user.getRole() == UserRole.ADMIN) {
                logger.info("Tried to promote an admin user:" + username);
                System.out.println("User is already an admin.");
                return;
            }
            UserPersistence.promoteUser(user);
            System.out.println("User promoted to admin.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the input.");
        }
    }

    @Override
    public boolean show() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;

        System.out.println("Welcome, " + user.getUsername() + "!");
        System.out.println("You are an admin.");

        while (running) {
            System.out.print("ADMIN > ");
            var input = br.readLine();
            var command = parseCommand(input);
            if (command == null) {
                System.out.println("Invalid command. Type /help for a list of commands.");
                continue;
            }

            switch (command) {
                case LOGOUT:
                    running = false;
                    break;
                case HELP:
                    showHelp();
                    break;
                case PROMOTE:
                    promoteUser();
                    break;
                case EXIT:
                    return false;
            }
        }

        return true;
    }


    @Override
    protected AdminUserCommand parseCommand(String command) {
        if (command == null) {
            return null;
        }
        command = command.trim().toLowerCase();
        if (command.isBlank()) {
            return null;
        }

        if (!command.startsWith("/")) {
            return null;
        }

        try {
            return AdminUserCommand.valueOf(command.replaceAll("/", "").toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public enum AdminUserCommand {
        LOGOUT,
        EXIT,
        PROMOTE,
        HELP,
    }
}
