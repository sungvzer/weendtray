package it.salvatoregargano.weendtray.terminal;

import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.acl.UserRole;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.logging.ConsoleLogger;
import it.salvatoregargano.weendtray.logging.LogLevel;

public class TerminalApplication {
    private static boolean adminUserPrompt() {
        if (UserPersistence.atLeastOneAdminUser()) {
            return true;
        }

        System.out.println("No admin users found. Do you want to create one? (Y/n)");
        var input = System.console().readLine();
        if (!input.equalsIgnoreCase("Y")) {
            return false;
        }

        System.out.println("Enter the username:");
        var username = System.console().readLine();
        System.out.println("Enter the password:");
        var password = new String(System.console().readPassword());
        var hashedPassword = User.hashPassword(password);
        System.out.println("Enter the email:");
        var email = System.console().readLine();
        System.out.println("Enter the name:");
        var name = System.console().readLine();
        System.out.println("Enter the surname:");
        var surname = System.console().readLine();

        UserPersistence.saveUser(new User(username, hashedPassword, email, name, surname, UserRole.ADMIN));
        return true;
    }

    public static void run(boolean debug) {
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

            running = false;
        }
    }
}
