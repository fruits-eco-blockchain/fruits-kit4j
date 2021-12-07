package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public class SubmitNonceResponse extends FRSResponse {
    private final String result;
    private final Long deadline;

    public SubmitNonceResponse(String result, Long deadline) {
        this.result = result;
        this.deadline = deadline;
    }

    public String getResult() {
        return result;
    }

    public Long getDeadline() {
        return deadline;
    }
}
