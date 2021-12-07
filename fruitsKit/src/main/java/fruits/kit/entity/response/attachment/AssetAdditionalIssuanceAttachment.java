package fruits.kit.entity.response.attachment;

import fruits.kit.entity.response.TransactionAttachment;

public class AssetAdditionalIssuanceAttachment extends TransactionAttachment {
    private String assetId;
    private String quantityQNT;

    public AssetAdditionalIssuanceAttachment(int version, String assetId, String quantityQNT) {
        super(version);
        this.assetId = assetId;
        this.quantityQNT = quantityQNT;
    }
    
    public String getAssetId() {
		return assetId;
	}


	public String getQuantityQNT() {
        return quantityQNT;
    }
}
