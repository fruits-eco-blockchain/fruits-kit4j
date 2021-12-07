package fruits.kit.entity.response.http.attachment;

import com.google.gson.annotations.SerializedName;

import fruits.kit.entity.response.TransactionAttachment;
import fruits.kit.entity.response.attachment.AssetAdditionalIssuanceAttachment;

public class AssetAdditionalIssuanceAttachmentResponse extends TransactionAttachmentResponse {
    @SerializedName("version.AssetAdditionalIssuance")
    private final int version;
    private String assetId;
    private String quantityQNT;

    public AssetAdditionalIssuanceAttachmentResponse(int version, String assetId, String quantityQNT) {
        this.version = version;
        this.assetId = assetId;
        this.quantityQNT = quantityQNT;
    }

    public int getVersion() {
        return version;
    }

    public String getAssetId() {
		return assetId;
	}

	public String getQuantityQNT() {
        return quantityQNT;
    }

    @Override
    public TransactionAttachment toAttachment() {
        return new AssetAdditionalIssuanceAttachment(version, assetId, quantityQNT);
    }
}
