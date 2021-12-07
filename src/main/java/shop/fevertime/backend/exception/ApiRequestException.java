package shop.fevertime.backend.exception;

public class ApiRequestException extends IllegalArgumentException { // NoSuchException으로 변경
    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}