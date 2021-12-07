package fruits.kit.entity.response.http.attachment;

import com.google.gson.annotations.SerializedName;

import fruits.kit.entity.response.TransactionAttachment;
import fruits.kit.entity.response.attachment.BidOrderCancellationAttachment;

public class BidOrderCancellationAttachmentResponse extends TransactionAttachmentResponse {
    @SerializedName("version.BidOrderCancellation")
    private final int version;
    private final String order;

    public BidOrderCancellationAttachmentResponse(int version, String order) {
        this.version = version;
        this.order = order;
    }

    public int getVersion() {
        return version;
    }

    public String getOrder() {
        return order;
    }

    @Override
    public TransactionAttachment toAttachment() {
        return new BidOrderCancellationAttachment(version, order);
    }
}
