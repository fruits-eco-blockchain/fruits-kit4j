package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public abstract class FRSResponse {
    private final String errorDescription;
    private final Integer errorCode;
    private final Integer requestProcessingTime;

    public FRSResponse(String errorDescription, Integer errorCode, Integer requestProcessingTime) {
        this.errorDescription = errorDescription;
        this.errorCode = errorCode;
        this.requestProcessingTime = requestProcessingTime;
    }

    public FRSResponse() {
        this.errorDescription = null;
        this.errorCode = null;
        this.requestProcessingTime = null;
    }

    public void throwIfError() throws FRSError {
        if (errorDescription != null) {
            throw new FRSError(errorCode, errorDescription);
        }
    }

    public Integer getRequestProcessingTime() {
        return requestProcessingTime;
    }
}
