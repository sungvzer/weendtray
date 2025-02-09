package it.salvatoregargano.weendtray.terminal;

import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.acl.UserRole;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.logging.ConsoleLogger;
import it.salvatoregargano.weendtray.logging.LogLevel;
import it.salvatoregargano.weendtray.terminal.screens.AdminUserScreen;
import it.salvatoregargano.weendtray.terminal.screens.RegularUserScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalApplication {
    private static boolean adminUserPrompt() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        if (UserPersistence.atLeastOneAdminUser()) {
            return true;
        }

        System.out.println("No admin users found. Do you want to create one? (Y/n)");
        var input = br.readLine();
        if (!input.equalsIgnoreCase("Y")) {
            return false;
        }

        System.out.println("Enter the username:");
        var username = br.readLine();
        System.out.println("Enter the password:");
        String hashedPassword;
        {
            var password = new String(System.console().readPassword());
            hashedPassword = User.hashPassword(password);
        }
        System.out.println("Enter the email:");
        var email = br.readLine();
        System.out.println("Enter the name:");
        var name = br.readLine();
        System.out.println("Enter the surname:");
        var surname = br.readLine();

        UserPersistence.saveUser(new User(username, hashedPassword, email, name, surname, UserRole.ADMIN));
        System.out.println("Admin user created.");
        return true;
    }

    /**
     * Prompts the user to log in.
     *
     * @return the logged-in user, or null if the login failed.
     * @throws IOException
     */
    private static User loginScreen() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter your username:");
        var username = br.readLine();
        System.out.println("Enter your password:");
        var password = new String(System.console().readPassword());

        var user = UserPersistence.getUserByUsername(username);
        if (user == null) {
            return null;
        }

        if (!user.verifyPassword(password)) {
            CombinedLogger.getInstance().info("User " + user.getId() + " (" + user.getUsername() + ") failed to log in.");
            return null;
        }
        return user;
    }

    public static void run(boolean debug) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        var logger = CombinedLogger.getInstance();

        if (debug) {
            CombinedLogger.getInstance().setLogLevel(LogLevel.DEBUG);
        } else {
            CombinedLogger.getInstance().setLogLevel(LogLevel.INFO);
        }
        logger.debug("Starting terminal application. Turning off terminal logging.");
        ConsoleLogger.getInstance().setLogLevel(LogLevel.OFF);
        logger.debug("Terminal logging turned off.");

        if (!adminUserPrompt()) {
            return;
        }

        boolean running = true;
        while (running) {
            System.out.print("home > ");
            var input = br.readLine();
            var command = parseCommand(input);
            if (command == null) {
                System.out.println("Invalid command. Type /help for a list of commands.");
                continue;
            }
            switch (command) {
                case EXIT:
                    running = false;
                    break;

                case LOGIN:
                    var user = loginScreen();
                    if (user == null) {
                        break;
                    }


                    running = switch (user.getRole()) {
                        case ADMIN -> new AdminUserScreen(user).show();
                        case USER -> new RegularUserScreen(user).show();
                    };
                    break;

                case HELP:
                    System.out.println("Available commands:");
                    System.out.println("/exit - Exits the application.");
                    System.out.println("/help - Shows this help message.");
                    System.out.println("/login - Logs in the user.");
                    System.out.println("/signup - Signs up the user.");
                    break;

                case SIGNUP:
                    break;
                case LOGOUT:
                    break;
                default:
                    System.out.println("Command not implemented yet.");
                    break;
            }
        }
    }

    private static Command parseCommand(String input) {
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
            return Command.valueOf(input.replaceAll("/", "").toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private enum Command {
        EXIT,
        HELP,
        LOGIN,
        SIGNUP,
        LOGOUT
    }
}
