package fruits.kit.entity.response.http.attachment;

import com.google.gson.annotations.SerializedName;

import fruits.kit.entity.response.TransactionAppendix;
import fruits.kit.entity.response.appendix.PlaintextMessageAppendix;

public class MessageAttachmentResponse extends TransactionAppendixResponse {
    private final String message;
    private final boolean messageIsText;
    @SerializedName("version.Message")
    private final int version;

    public MessageAttachmentResponse(String message, boolean messageIsText, int version) {
        this.message = message;
        this.messageIsText = messageIsText;
        this.version = version;
    }

    public String getMessage() {
        return message;
    }

    public boolean isMessageIsText() {
        return messageIsText;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public TransactionAppendix toAppendix() {
        return new PlaintextMessageAppendix(version, message, messageIsText);
    }
}
