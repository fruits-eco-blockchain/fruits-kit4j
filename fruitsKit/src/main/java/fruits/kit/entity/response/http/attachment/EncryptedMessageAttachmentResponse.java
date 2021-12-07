package fruits.kit.entity.response.http.attachment;

import com.google.gson.annotations.SerializedName;

import fruits.kit.entity.response.TransactionAppendix;
import fruits.kit.entity.response.appendix.EncryptedMessageAppendix;
import fruits.kit.entity.response.http.EncryptedMessageResponse;

public class EncryptedMessageAttachmentResponse extends TransactionAppendixResponse {
    private final EncryptedMessageResponse encryptedMessage;
    @SerializedName("version.EncryptedMessage")
    private final int version;

    public EncryptedMessageAttachmentResponse(EncryptedMessageResponse encryptedMessage, int version) {
        this.encryptedMessage = encryptedMessage;
        this.version = version;
    }

    public EncryptedMessageResponse getEncryptedMessage() {
        return encryptedMessage;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public TransactionAppendix toAppendix() {
        return new EncryptedMessageAppendix.ToRecipient(version, encryptedMessage.toEncryptedMessage());
    }
}
