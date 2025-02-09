package it.salvatoregargano.weendtray.terminal.screens;

import it.salvatoregargano.weendtray.acl.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegularUserScreen extends UserScreen<RegularUserScreen.RegularUserCommand> {
    public RegularUserScreen(User user) {
        super(user);
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
                case EXIT:
                    return false;
            }
        }

        return true;
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
        EXIT
    }
}
