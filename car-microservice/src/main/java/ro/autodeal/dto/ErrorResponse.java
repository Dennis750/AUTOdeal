package ro.autodeal.dto;

public class ErrorResponse {

    private String message;
    private String errorCode;
    private String timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, String errorCode, String timestamp) {
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}