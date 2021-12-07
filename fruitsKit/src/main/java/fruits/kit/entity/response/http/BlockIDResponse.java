package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class BlockIDResponse extends FRSResponse {
    private final String block;

    public BlockIDResponse(String block) {
        this.block = block;
    }

    public String getBlockID() {
        return block;
    }
}
