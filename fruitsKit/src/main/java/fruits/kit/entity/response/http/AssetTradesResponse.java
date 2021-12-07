package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class AssetTradesResponse extends FRSResponse {
    private final TradeResponse[] trades;

    public AssetTradesResponse(TradeResponse[] assetTrades) {
        this.trades = assetTrades;
    }

    public TradeResponse[] getTrades() {
        return trades;
    }
}
