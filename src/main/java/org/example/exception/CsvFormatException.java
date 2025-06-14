package org.example.exception;

public class CsvFormatException extends IllegalArgumentException {
    public CsvFormatException(String message) {
        super(message);
    }
}
