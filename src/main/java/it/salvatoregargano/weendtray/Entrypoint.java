package it.salvatoregargano.weendtray;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.persistence.MigrationRunner;
import it.salvatoregargano.weendtray.terminal.TerminalApplication;
import picocli.CommandLine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "weendtray", mixinStandardHelpOptions = true, version = Entrypoint.version)
public class Entrypoint implements Callable<Integer> {
    public static final String executionId = UUID.randomUUID().toString();
    private static final int MAJOR_VERSION = 1;
    private static final int MINOR_VERSION = 0;
    private static final int PATCH_VERSION = 0;
    public static final String version = MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION;

    @CommandLine.Option(names = "--gui", description = "Start the application in GUI mode.")
    private boolean guiMode = false;

    @CommandLine.Option(names = {"-d", "--debug"}, description = "Enable debug logging.")
    private boolean debug = false;


    public static void main(String[] args) {
        try {
            MigrationRunner.runMigrations();
        } catch (SQLException | IOException | URISyntaxException e) {
            CombinedLogger.getInstance().error("An error occurred while running the migrations.\n" + Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }
        // Create and execute the command line application.
        int exitCode = new CommandLine(new Entrypoint()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        if (!guiMode) {
            try {
                new TerminalApplication().run(debug);
            } catch (Exception e) {
                CombinedLogger.getInstance().error("An error occurred while running the terminal application." + e);
                return 1;
            }
            return 0;
        }
        return 0;
    }
}