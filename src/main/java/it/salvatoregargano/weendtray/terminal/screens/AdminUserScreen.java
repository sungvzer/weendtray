package it.salvatoregargano.weendtray.terminal.screens;

import it.salvatoregargano.weendtray.acl.*;
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
        System.out.println("/create_user - Create a new user.");
        System.out.println("/help - Show this help message.");
        System.out.println("/toggle_user - Toggle a user's active status.");
    }

    private void createUser() {
        var logger = CombinedLogger.getInstance();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Creating a new user.");
        try {
            System.out.print("Username: ");
            var username = br.readLine();
            var user = UserPersistence.getUserByUsername(username);
            if (user != null) {
                logger.info("Tried to create an existing user: " + username);
                System.out.println("User already exists.");
                return;
            }

            System.out.print("Password: ");
            var password = new String(System.console().readPassword());
            var hashedPassword = User.hashPassword(password);

            System.out.print("Name: ");
            var name = br.readLine();

            System.out.print("Surname: ");
            var surname = br.readLine();

            System.out.print("Phone number: ");
            var phoneNumber = br.readLine();

            UserPersistence.saveUser(new RegularUser(username, hashedPassword, name, surname, PhonePlan.REGULAR, phoneNumber, true));
            System.out.println("User created.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the input.");
        }

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
                case CREATE_USER:
                    createUser();
                    break;
                case EXIT:
                    return false;
                case TOGGLE_USER:
                    toggleUser();
                    break;
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

    private void toggleUser() throws IOException {
        var logger = CombinedLogger.getInstance();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the username of the user you want to toggle: ");
        try {
            var username = br.readLine();
            var user = UserPersistence.getUserByUsername(username);
            if (user == null) {
                logger.info("Tried to toggle non-existing user:" + username);
                System.out.println("User does not exist.");
                return;
            }
            if (user.getRole() == UserRole.ADMIN) {
                logger.warn("Tried to toggle an admin user:" + username);
                System.out.println("User is an admin.");
                return;
            }
            UserPersistence.toggleUser(user);
            System.out.println("User toggled.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the input.");
        }
    }

    public enum AdminUserCommand {
        LOGOUT,
        EXIT,
        PROMOTE,
        CREATE_USER,
        HELP,
        TOGGLE_USER
    }
}
