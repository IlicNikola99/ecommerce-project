package code.ecommerceproject.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetails(HttpStatus status, String message, String details) {
        super();
        this.timestamp = new Date();
        this.message = message;
        this.details = details;
    }

    // Getters and setters
    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
} 