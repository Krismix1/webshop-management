package dk.cristi.app.webshop.management.models.domain;

import java.util.Date;

public class RestErrorInfo {
    private String message;
    private String details;
    private Date timestamp;

    public RestErrorInfo(Date timestamp, String message, String details) {
        super();
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
