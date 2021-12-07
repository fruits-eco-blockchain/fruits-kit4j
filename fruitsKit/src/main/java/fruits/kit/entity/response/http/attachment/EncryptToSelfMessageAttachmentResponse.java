package fruits.kit.entity.response.http.attachment;

import com.google.gson.annotations.SerializedName;

import fruits.kit.entity.response.TransactionAppendix;
import fruits.kit.entity.response.appendix.EncryptedMessageAppendix;
import fruits.kit.entity.response.http.EncryptedMessageResponse;

public class EncryptToSelfMessageAttachmentResponse extends TransactionAppendixResponse {
    private final EncryptedMessageResponse encryptToSelfMessage;
    @SerializedName("version.EncryptToSelfMessage")
    private final int version;

    public EncryptToSelfMessageAttachmentResponse(EncryptedMessageResponse encryptToSelfMessage, int version) {
        this.encryptToSelfMessage = encryptToSelfMessage;
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public EncryptedMessageResponse getEncryptToSelfMessage() {
        return encryptToSelfMessage;
    }

    @Override
    public TransactionAppendix toAppendix() {
        return new EncryptedMessageAppendix.ToSelf(version, encryptToSelfMessage.toEncryptedMessage());
    }
}
