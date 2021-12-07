package fruits.kit.entity.response;

import org.bouncycastle.util.encoders.Hex;

import fruits.kit.crypto.FruitsCrypto;
import fruits.kit.entity.FruitsID;
import fruits.kit.entity.response.http.BroadcastTransactionResponse;

public class TransactionBroadcast {
    private final byte[] fullHash;
    private final FruitsID transactionId;
    private final int numberPeersSentTo;

    public TransactionBroadcast(byte[] fullHash, FruitsID transactionId, int numberPeersSentTo) {
        this.fullHash = fullHash;
        this.transactionId = transactionId;
        this.numberPeersSentTo = numberPeersSentTo;
    }

    public TransactionBroadcast(BroadcastTransactionResponse response) {
        this.fullHash = Hex.decode(response.getFullHash());
        this.transactionId = FruitsID.fromLong(response.getTransactionID());
        this.numberPeersSentTo = response.getNumberPeersSentTo();
    }

    public byte[] getFullHash() {
        return fullHash;
    }

    public FruitsID getTransactionId() {
        return transactionId;
    }

    public int getNumberPeersSentTo() {
        return numberPeersSentTo;
    }
}
