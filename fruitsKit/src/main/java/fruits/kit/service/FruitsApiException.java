package fruits.kit.service;

public class FruitsApiException extends RuntimeException {
    public FruitsApiException(String message) {
        super("Fruits API Error, error description: " + message);
    }
}
