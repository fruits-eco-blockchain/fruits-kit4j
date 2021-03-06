package fruits.kit.entity.response.http.attachment;

import com.google.gson.annotations.SerializedName;

import fruits.kit.entity.response.TransactionAttachment;
import fruits.kit.entity.response.attachment.AskOrderPlacementAttachment;

public class AskOrderPlacementAttachmentResponse extends TransactionAttachmentResponse {
    @SerializedName("version.AskOrderPlacement")
    private final int version;
    private final String asset;
    private final String quantityQNT;
    private final String priceNQT;

    public AskOrderPlacementAttachmentResponse(int version, String asset, String quantityQNT, String priceNQT) {
        this.version = version;
        this.asset = asset;
        this.quantityQNT = quantityQNT;
        this.priceNQT = priceNQT;
    }

    public int getVersion() {
        return version;
    }

    public String getAsset() {
        return asset;
    }

    public String getQuantityQNT() {
        return quantityQNT;
    }

    public String getPriceNQT() {
        return priceNQT;
    }

    @Override
    public TransactionAttachment toAttachment() {
        return new AskOrderPlacementAttachment(version, asset, quantityQNT, priceNQT);
    }
}
