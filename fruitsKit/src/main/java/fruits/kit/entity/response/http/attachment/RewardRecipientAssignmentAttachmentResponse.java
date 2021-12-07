package fruits.kit.entity.response.http.attachment;

import com.google.gson.annotations.SerializedName;

import fruits.kit.entity.response.TransactionAttachment;
import fruits.kit.entity.response.attachment.RewardRecipientAssignmentAttachment;

@SuppressWarnings("WeakerAccess")
public final class RewardRecipientAssignmentAttachmentResponse extends TransactionAttachmentResponse {
    @SerializedName("version.RewardRecipientAssignment")
    private final int version;

    public RewardRecipientAssignmentAttachmentResponse(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public TransactionAttachment toAttachment() {
        return new RewardRecipientAssignmentAttachment(version);
    }
}
