package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class AtIDsResponse extends FRSResponse {
    private final String[] atIds;

    public AtIDsResponse(String[] atIds) {
        this.atIds = atIds;
    }

    public String[] getAtIds() {
        return atIds;
    }
}
