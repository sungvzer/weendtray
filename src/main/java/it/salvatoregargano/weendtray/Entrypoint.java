package it.salvatoregargano.weendtray;

import it.salvatoregargano.weendtray.terminal.TerminalApplication;
import picocli.CommandLine;

import java.util.UUID;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "weendtray", mixinStandardHelpOptions = true, version = Entrypoint.version)
public class Entrypoint implements Callable<Integer> {
    public static final String executionId = UUID.randomUUID().toString();
    private static final int MAJOR_VERSION = 1;
    private static final int MINOR_VERSION = 0;
    private static final int PATCH_VERSION = 0;
    public static final String version = MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION;

    // Define the --gui option.
    @CommandLine.Option(names = "--gui", description = "Start the application in GUI mode.")
    private boolean guiMode;

    public static void main(String[] args) {
        // Create and execute the command line application.
        int exitCode = new CommandLine(new Entrypoint()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        if (!guiMode) {
            TerminalApplication.run();
            return 0;
        }
        return 0;
    }
}