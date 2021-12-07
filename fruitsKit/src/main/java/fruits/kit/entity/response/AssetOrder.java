package fruits.kit.entity.response;

import java.util.Locale;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsID;
import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.response.http.OrderResponse;
import fruits.kit.service.FruitsApiException;

public class AssetOrder {
    private final FruitsID id;
    private final FruitsID assetId;
    private final FruitsAddress accountAddress;
    /**
     * Quantity of the asset. Not actually in Fruits; The FruitsValue class is used as a utility.
     * Actually measured in terms of now many of the specific asset there are.
     */
    private final FruitsValue quantity;
    /**
     * Price per asset.
     */
    private final FruitsValue price;
    private final int height;
    private final OrderType type;

    public AssetOrder(FruitsID id, FruitsID assetId, FruitsAddress accountAddress, FruitsValue quantity, FruitsValue price, int height, OrderType type) {
        this.id = id;
        this.assetId = assetId;
        this.accountAddress = accountAddress;
        this.quantity = quantity;
        this.price = price;
        this.height = height;
        this.type = type;
    }

    public AssetOrder(OrderResponse orderResponse) {
        this.id = FruitsID.fromLong(orderResponse.getOrder());
        this.assetId = FruitsID.fromLong(orderResponse.getAsset());
        this.accountAddress = FruitsAddress.fromId(orderResponse.getAccount());
        this.quantity = FruitsValue.fromPlanck(orderResponse.getQuantityQNT());
        this.price = FruitsValue.fromPlanck(orderResponse.getPriceNQT());
        this.height = orderResponse.getHeight();
        this.type = OrderType.parse(orderResponse.getType());
    }

    public FruitsID getId() {
        return id;
    }

    public FruitsID getAssetId() {
        return assetId;
    }

    public FruitsAddress getAccountAddress() {
        return accountAddress;
    }

    public FruitsValue getQuantity() {
        return quantity;
    }

    public FruitsValue getPrice() {
        return price;
    }

    public int getHeight() {
        return height;
    }

    public OrderType getType() {
        return type;
    }

    public enum OrderType {
        ASK,
        BID,
        ;

        public static OrderType parse(String orderType) {
            switch (orderType.toLowerCase(Locale.ENGLISH).trim()) {
                case "ask": return ASK;
                case "bid": return BID;
                default: throw new FruitsApiException("Could not parse Order Type " + orderType);
            }
        }
    }
}
