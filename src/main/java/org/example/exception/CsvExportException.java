package org.example.exception;

public class CsvExportException extends RuntimeException {
    public CsvExportException(String message) {
        super(message);
    }
    public CsvExportException(String message, Exception exception) {
        super(message, exception);
    };
}
