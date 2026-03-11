package it.salvatoregargano.weendtray.utils;

public class StringChecker {
    private int upperCount;
    private String upperMessage;
    private int digitCount;
    private String digitMessage;
    private int minLength;
    private String minLengthMessage;
    private int maxLength;
    private String maxLengthMessage;

    public StringChecker() {
        upperCount = 0;
        digitCount = 0;
        minLength = 0;
        maxLength = Integer.MAX_VALUE;
    }

    public StringChecker withMaxLength(int length, String message) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be non-negative");
        }
        this.maxLength = length;
        this.maxLengthMessage = message;
        return this;
    }

    public StringChecker withExactLength(int length, String message) {
        return withMinLength(length, message).withMaxLength(length, message);
    }

    public StringChecker withUpper(int upperCount, String message) {
        if (upperCount < 0) {
            throw new IllegalArgumentException("upperCount must be non-negative");
        }
        this.upperCount = upperCount;
        this.upperMessage = message;
        return this;
    }

    public StringChecker withDigits(int digitCount, String message) {
        if (digitCount < 0) {
            throw new IllegalArgumentException("digitCount must be non-negative");
        }
        this.digitCount = digitCount;
        this.digitMessage = message;
        return this;
    }

    public StringChecker withMinLength(int length, String message) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be non-negative");
        }
        this.minLength = length;
        this.minLengthMessage = message;
        return this;
    }

    public String check(String value) {
        if (value.length() < minLength) {
            return minLengthMessage != null ? minLengthMessage
                    : "La stringa deve essere lunga almeno " + minLength + " caratteri.";
        }
        if (value.length() > maxLength) {
            return maxLengthMessage != null ? maxLengthMessage
                    : "La stringa deve essere lunga al massimo " + maxLength + " caratteri.";
        }
        int upper = 0;
        int digit = 0;
        for (char c : value.toCharArray()) {
            if (Character.isUpperCase(c)) {
                upper++;
            }
            if (Character.isDigit(c)) {
                digit++;
            }
        }
        if (upper < upperCount) {
            return upperMessage != null ? upperMessage
                    : "La stringa deve contenere almeno " + upperCount + " lettere maiuscole.";
        }
        if (digit < digitCount) {
            return digitMessage != null ? digitMessage
                    : "La stringa deve contenere almeno " + digitCount + " cifre.";
        }
        return null;
    }

    public static StringChecker passwordChecker() {
        return new StringChecker().withMinLength(8, "La password deve essere lunga almeno 8 caratteri.")
                .withUpper(1, "La password deve contenere almeno una lettera maiuscola.")
                .withDigits(1, "La password deve contenere almeno una cifra.");
    }

    public static StringChecker phoneNumberChecker() {
        return new StringChecker().withExactLength(10, "Il numero di telefono deve essere composto da 10 cifre.")
                .withDigits(10, "Il numero di telefono deve essere composto solo da cifre.");
    }

}
