package fruits.kit.entity.response.http.attachment;

import com.google.gson.annotations.SerializedName;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.response.TransactionAttachment;
import fruits.kit.entity.response.attachment.MultiOutSameAttachment;

import java.util.Arrays;

public final class MultiOutSameAttachmentResponse extends TransactionAttachmentResponse {
    @SerializedName("version.MultiSameOutCreation")
    private final int version;
    private final String[] recipients;

    public MultiOutSameAttachmentResponse(int version, String[] recipients) {
        this.version = version;
        this.recipients = recipients;
    }

    public int getVersion() {
        return version;
    }

    public String[] getRecipients() {
        return recipients;
    }

    @Override
    public TransactionAttachment toAttachment() {
        return new MultiOutSameAttachment(version, Arrays.stream(recipients)
                .map(FruitsAddress::fromId)
                .toArray(FruitsAddress[]::new));
    }
}
