package fruits.kit.entity.response;

import java.util.Locale;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsID;
import fruits.kit.entity.FruitsTimestamp;
import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.response.http.TradeResponse;
import fruits.kit.service.FruitsApiException;

public class AssetTrade {
    private final FruitsTimestamp timestamp;
    /**
     * Quantity of the asset. Not actually in Fruits; The FruitsValue class is used as a utility.
     * Actually measured in terms of now many of the specific asset there are.
     */
    private final FruitsValue quantity; // TODO update this to be an AssetValue or similar class that takes into account the actual number of decimals of this asset
    /**
     * Price per asset.
     */
    private final FruitsValue price;
    private final FruitsID assetId;
    private final FruitsID askOrderId;
    private final FruitsID bidOrderId;
    private final int askOrderHeight;
    private final FruitsAddress sellerAddress;
    private final FruitsAddress buyerAddress;
    private final FruitsID blockId;
    private final int height;
    private final TradeType type;
    private final String assetName;
    private final int assetDecimals;

    public AssetTrade(FruitsTimestamp timestamp, FruitsValue quantity, FruitsValue price, FruitsID assetId, FruitsID askOrderId, FruitsID bidOrderId, int askOrderHeight, FruitsAddress sellerAddress, FruitsAddress buyerAddress, FruitsID blockId, int height, TradeType type, String assetName, int assetDecimals) {
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.price = price;
        this.assetId = assetId;
        this.askOrderId = askOrderId;
        this.bidOrderId = bidOrderId;
        this.askOrderHeight = askOrderHeight;
        this.sellerAddress = sellerAddress;
        this.buyerAddress = buyerAddress;
        this.blockId = blockId;
        this.height = height;
        this.type = type;
        this.assetName = assetName;
        this.assetDecimals = assetDecimals;
    }

    public AssetTrade(TradeResponse tradeResponse) {
        this.timestamp = FruitsTimestamp.fromFruitsTimestamp(tradeResponse.getTimestamp());
        this.quantity = FruitsValue.fromPlanck(tradeResponse.getQuantityQNT());
        this.price = FruitsValue.fromPlanck(tradeResponse.getPriceNQT());
        this.assetId = FruitsID.fromLong(tradeResponse.getAsset());
        this.askOrderId = FruitsID.fromLong(tradeResponse.getAskOrder());
        this.bidOrderId = FruitsID.fromLong(tradeResponse.getBidOrder());
        this.askOrderHeight = tradeResponse.getAskOrderHeight();
        this.sellerAddress = FruitsAddress.fromId(tradeResponse.getSeller());
        this.buyerAddress = FruitsAddress.fromId(tradeResponse.getBuyer());
        this.blockId = FruitsID.fromLong(tradeResponse.getBlock());
        this.height = tradeResponse.getHeight();
        this.type = TradeType.parse(tradeResponse.getTradeType());
        this.assetName = tradeResponse.getName();
        this.assetDecimals = tradeResponse.getDecimals();
    }
    
    public FruitsTimestamp getTimestamp() {
        return timestamp;
    }

    public FruitsValue getQuantity() {
        return quantity;
    }

    public FruitsValue getPrice() {
        return price;
    }

    public FruitsID getAssetId() {
        return assetId;
    }

    public FruitsID getAskOrderId() {
        return askOrderId;
    }

    public FruitsID getBidOrderId() {
        return bidOrderId;
    }

    public int getAskOrderHeight() {
        return askOrderHeight;
    }

    public FruitsAddress getSellerAddress() {
        return sellerAddress;
    }

    public FruitsAddress getBuyerAddress() {
        return buyerAddress;
    }

    public FruitsID getBlockId() {
        return blockId;
    }

    public int getHeight() {
        return height;
    }

    public TradeType getType() {
        return type;
    }

    public String getAssetName() {
        return assetName;
    }

    public int getAssetDecimals() {
        return assetDecimals;
    }

    public enum TradeType {
        BUY,
        SELL,
        ;

        public static TradeType parse(String tradeType) {
            switch (tradeType.toLowerCase(Locale.ENGLISH).trim()) {
                case "buy": return BUY;
                case "sell": return SELL;
                default: throw new FruitsApiException("Could not parse Trade Type " + tradeType);
            }
        }
    }
}
