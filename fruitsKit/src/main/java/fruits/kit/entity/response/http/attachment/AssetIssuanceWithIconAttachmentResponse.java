package fruits.kit.entity.response.http.attachment;

import com.google.gson.annotations.SerializedName;

import fruits.kit.entity.response.TransactionAttachment;
import fruits.kit.entity.response.attachment.AssetIssuanceAttachment;
import fruits.kit.entity.response.attachment.AssetIssuanceWithIconAttachment;

public class AssetIssuanceWithIconAttachmentResponse extends TransactionAttachmentResponse {
    @SerializedName("version.AssetIssuanceWithIcon")
    private final int version;
    private String name;
    private String description;
    private int decimals;
    private String quantityQNT;
    private String icon;

    public AssetIssuanceWithIconAttachmentResponse(int version, String name, String description, int decimals, String quantityQNT, String icon) {
        this.version = version;
        this.name = name;
        this.description = description;
        this.decimals = decimals;
        this.quantityQNT = quantityQNT;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDecimals() {
        return decimals;
    }

    public String getQuantityQNT() {
        return quantityQNT;
    }

    @Override
    public TransactionAttachment toAttachment() {
        return new AssetIssuanceWithIconAttachment(version, name, description, decimals, quantityQNT, icon);
    }
}
