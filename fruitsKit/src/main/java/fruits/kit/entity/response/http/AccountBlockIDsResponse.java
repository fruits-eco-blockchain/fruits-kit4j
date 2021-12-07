package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class AccountBlockIDsResponse extends FRSResponse {
    private final String[] blockIds;

    public AccountBlockIDsResponse(String[] blockIds) {
        this.blockIds = blockIds;
    }

    public String[] getBlockIds() {
        return blockIds;
    }
}
