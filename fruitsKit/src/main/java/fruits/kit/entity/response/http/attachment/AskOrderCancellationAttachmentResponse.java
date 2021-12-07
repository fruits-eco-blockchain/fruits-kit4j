package fruits.kit.entity.response.http.attachment;

import com.google.gson.annotations.SerializedName;

import fruits.kit.entity.response.TransactionAttachment;
import fruits.kit.entity.response.attachment.AskOrderCancellationAttachment;

public class AskOrderCancellationAttachmentResponse extends TransactionAttachmentResponse {
    @SerializedName("version.AskOrderCancellation")
    private final int version;
    private final String order;

    public AskOrderCancellationAttachmentResponse(int version, String order) {
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
        return new AskOrderCancellationAttachment(version, order);
    }
}
