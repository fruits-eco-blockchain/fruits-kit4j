package fruits.kit.entity.response.http;

import org.bouncycastle.util.encoders.Hex;

import fruits.kit.entity.FruitsEncryptedMessage;

public class EncryptedMessageResponse {
    private final String data;
    private final String nonce;
    private final boolean isText;

    public EncryptedMessageResponse(String data, String nonce, boolean isText) {
        this.data = data;
        this.nonce = nonce;
        this.isText = isText;
    }

    public FruitsEncryptedMessage toEncryptedMessage() {
        return new FruitsEncryptedMessage(Hex.decode(data), Hex.decode(nonce), isText);
    }

    public String getData() {
        return data;
    }

    public String getNonce() {
        return nonce;
    }

    public boolean isText() {
        return isText;
    }
}
