package fruits.kit.entity.response.attachment;

import fruits.kit.entity.response.TransactionAttachment;

public class AssetTransferAttachment extends TransactionAttachment {
    private String asset;
    private String quantityQNT;

    public AssetTransferAttachment(int version, String asset, String quantityQNT) {
        super(version);
        this.asset = asset;
        this.quantityQNT = quantityQNT;
    }

    //TODO constructor for FnsApi
    public String getAsset() {
        return asset;
    }

    public String getQuantityQNT() {
        return quantityQNT;
    }
}
