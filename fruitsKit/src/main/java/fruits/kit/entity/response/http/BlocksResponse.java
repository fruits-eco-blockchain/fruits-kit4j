package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class BlocksResponse extends FRSResponse {
    private final BlockResponse[] blocks;

    public BlocksResponse(BlockResponse[] blocks) {
        this.blocks = blocks;
    }

    public BlockResponse[] getBlocks() {
        return blocks;
    }
}
