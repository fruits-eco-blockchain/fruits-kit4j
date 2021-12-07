package fruits.kit.entity.response.http;

public class FRSError extends Exception {
    private final int code;
    private final String description;

    public FRSError(int code, String description) {
        super("FRS returned error code " + code + ": " + description);
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
