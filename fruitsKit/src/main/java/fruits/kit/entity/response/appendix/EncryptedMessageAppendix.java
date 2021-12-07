package fruits.kit.entity.response.appendix;

import fruits.kit.entity.FruitsEncryptedMessage;
import fruits.kit.entity.response.TransactionAppendix;

public abstract class EncryptedMessageAppendix extends TransactionAppendix {
    private final FruitsEncryptedMessage encryptedMessage;

    private EncryptedMessageAppendix(int version, FruitsEncryptedMessage encryptedMessage) {
        super(version);
        this.encryptedMessage = encryptedMessage;
    }

    public FruitsEncryptedMessage getEncryptedMessage() {
        return encryptedMessage;
    }

    public static class ToRecipient extends EncryptedMessageAppendix {
        public ToRecipient(int version, FruitsEncryptedMessage encryptedMessage) {
            super(version, encryptedMessage);
        }
    }

    public static class ToSelf extends EncryptedMessageAppendix {
        public ToSelf(int version, FruitsEncryptedMessage encryptedMessage) {
            super(version, encryptedMessage);
        }
    }
}
