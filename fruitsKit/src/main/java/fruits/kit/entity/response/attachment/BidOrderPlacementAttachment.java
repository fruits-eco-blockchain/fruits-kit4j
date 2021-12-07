package fruits.kit.entity.response.attachment;

import fruits.kit.entity.response.TransactionAttachment;

public class BidOrderPlacementAttachment extends TransactionAttachment {
    private String asset;
    private String quantityQNT;
    private String priceNQT;

    public BidOrderPlacementAttachment(int version, String asset, String quantityQNT, String priceNQT) {
        super(version);
        this.asset = asset;
        this.quantityQNT = quantityQNT;
        this.priceNQT = priceNQT;
    }
    //TODO constructor for FnsApi

    public String getAsset() {
        return asset;
    }

    public String getQuantityQNT() {
        return quantityQNT;
    }

    public String getPriceNQT() {
        return priceNQT;
    }
}
