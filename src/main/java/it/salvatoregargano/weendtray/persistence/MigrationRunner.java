package it.salvatoregargano.weendtray.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class MigrationRunner {

    /**
     * Gets the latest migration that was run.
     *
     * @return The latest migration filename or null if no migrations have been applied.
     */
    private static String getLatestMigration() throws SQLException {
        final var connection = DatabaseConnection.getInstance().getConnection();
        try (
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery("SELECT file FROM migrations ORDER BY file DESC LIMIT 1")
        ) {
            return resultSet.next() ? resultSet.getString("file") : null;
        }
    }

    /**
     * Executes an SQL script inside a transaction.
     *
     * @param sql The SQL script content.
     * @throws SQLException If an error occurs.
     */
    private static void executeSQL(String sql) throws SQLException {
        var connection = DatabaseConnection.getInstance().getConnection();
        try (var stmt = connection.createStatement()) {


            String[] queries = sql.split(";");
            for (String query : queries) {
                String trimmedQuery = query.trim();
                if (!trimmedQuery.isEmpty()) {
                    stmt.execute(trimmedQuery);
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error executing SQL", e);
        }
    }

    /**
     * Runs all SQL migration scripts from the migrations folder based on the latest applied migration.
     */
    public static void runMigrations() throws SQLException, IOException, URISyntaxException {
        var connection = DatabaseConnection.getInstance().getConnection();
        try (var statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS migrations (id INTEGER PRIMARY KEY, file TEXT)");
        }

        final var latestMigration = getLatestMigration();

        List<String> sqlFiles = getResourceFiles();
        sqlFiles.sort(Comparator.naturalOrder()); // Sort by filename to maintain order

        for (String fileName : sqlFiles) {
            if (latestMigration == null || fileName.compareTo(latestMigration) > 0) {
                try {
                    String sqlContent = loadSQLFile("migrations/" + fileName);
                    executeSQL(sqlContent);
                    logMigration(fileName);
                } catch (Exception e) {
                    throw new SQLException("Error running migration: " + fileName, e);
                }
            }
        }
    }

    /**
     * Reads an SQL file from the resources folder.
     *
     * @param filePath The file path inside resources.
     * @return The SQL script content as a String.
     */
    private static String loadSQLFile(String filePath) {
        ClassLoader classLoader = MigrationRunner.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {

            return reader.lines().collect(Collectors.joining("\n"));

        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Error loading SQL file: " + filePath, e);
        }
    }

    /**
     * Retrieves all SQL migration file names from the resources folder.
     *
     * @return List of file names.
     */
    private static List<String> getResourceFiles() throws IOException, URISyntaxException {
        ClassLoader classLoader = MigrationRunner.class.getClassLoader();
        URL resource = classLoader.getResource("migrations");

        if (resource == null) {
            throw new IllegalArgumentException("Resource folder not found: migrations");
        }

        if (resource.getProtocol().equals("jar")) {
            // Running from a JAR file
            String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
            try (JarFile jarFile = new JarFile(jarPath)) {
                return jarFile.stream()
                        .map(JarEntry::getName)
                        .filter(name -> name.startsWith("migrations/") && name.endsWith(".sql"))
                        .map(name -> name.substring("migrations/".length()))
                        .collect(Collectors.toList());
            }
        } else {
            // Running from the file system
            Path path = Paths.get(resource.toURI());
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, "*.sql")) {
                List<String> fileNames = new ArrayList<>();
                for (Path file : directoryStream) {
                    fileNames.add(file.getFileName().toString());
                }
                return fileNames;
            }
        }
    }

    /**
     * Logs a migration as executed in the database.
     *
     * @param migrationId The full filename of the migration.
     */
    private static void logMigration(String migrationId) throws SQLException {
        var connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO migrations (file) VALUES (?)")) {
            stmt.setString(1, migrationId);
            stmt.execute();

            ResultSet rs = stmt.getGeneratedKeys();
        }
    }

}
