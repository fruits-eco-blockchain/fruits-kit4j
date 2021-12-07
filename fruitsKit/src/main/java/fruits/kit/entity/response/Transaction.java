package fruits.kit.entity.response;

import org.bouncycastle.util.encoders.Hex;

import fruits.kit.crypto.FruitsCrypto;
import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsID;
import fruits.kit.entity.FruitsTimestamp;
import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.response.attachment.OrdinaryPaymentAttachment;
import fruits.kit.entity.response.http.TransactionResponse;
import fruits.kit.entity.response.http.attachment.TransactionAppendixResponse;

import java.util.Arrays;

public class Transaction {
    private final FruitsAddress recipient;
    private final FruitsAddress sender;
    private final FruitsID blockId;
    private final FruitsID ecBlockId;
    private final FruitsID id;
    private final FruitsTimestamp blockTimestamp;
    private final FruitsTimestamp timestamp;
    private final FruitsValue amount;
    private final FruitsValue fee;
    private final byte[] fullHash;
    private final byte[] referencedTransactionFullHash;
    private final byte[] senderPublicKey;
    private final byte[] signature;
    private final byte[] signatureHash;
    private final int blockHeight;
    private final int confirmations;
    private final int ecBlockHeight;
    private final int subtype;
    private final int type;
    private final int version;
    private final TransactionAttachment attachment;
    private final TransactionAppendix[] appendages;
    private final short deadline;

    public Transaction(FruitsAddress recipient, FruitsAddress sender, FruitsID blockId, FruitsID ecBlockId, FruitsID id, FruitsTimestamp blockTimestamp, FruitsTimestamp timestamp, FruitsValue amount, FruitsValue fee, byte[] fullHash, byte[] referencedTransactionFullHash, byte[] senderPublicKey, byte[] signature, byte[] signatureHash, int blockHeight, int confirmations, int ecBlockHeight, int subtype, int type, int version, TransactionAttachment attachment, TransactionAppendix[] appendages, short deadline) {
        this.recipient = recipient;
        this.sender = sender;
        this.blockId = blockId;
        this.ecBlockId = ecBlockId;
        this.id = id;
        this.blockTimestamp = blockTimestamp;
        this.timestamp = timestamp;
        this.amount = amount;
        this.fee = fee;
        this.fullHash = fullHash;
        this.referencedTransactionFullHash = referencedTransactionFullHash;
        this.senderPublicKey = senderPublicKey;
        this.signature = signature;
        this.signatureHash = signatureHash;
        this.blockHeight = blockHeight;
        this.confirmations = confirmations;
        this.ecBlockHeight = ecBlockHeight;
        this.subtype = subtype;
        this.type = type;
        this.version = version;
        this.attachment = attachment;
        this.appendages = appendages;
        this.deadline = deadline;
    }

    public Transaction(TransactionResponse transactionResponse) {
        this.recipient = FruitsAddress.fromEither(transactionResponse.getRecipient());
        this.sender = FruitsAddress.fromEither(transactionResponse.getSender());
        this.blockId = FruitsID.fromLong(transactionResponse.getBlock());
        this.ecBlockId = FruitsID.fromLong(transactionResponse.getEcBlockId());
        this.id = FruitsID.fromLong(transactionResponse.getTransaction());
        this.blockTimestamp = FruitsTimestamp.fromFruitsTimestamp(transactionResponse.getBlockTimestamp());
        this.timestamp = FruitsTimestamp.fromFruitsTimestamp(transactionResponse.getTimestamp());
        this.amount = FruitsValue.fromPlanck(transactionResponse.getAmountNQT());
        this.fee = FruitsValue.fromPlanck(transactionResponse.getFeeNQT());
        this.fullHash = Hex.decode(transactionResponse.getFullHash());
        this.referencedTransactionFullHash = transactionResponse.getReferencedTransactionFullHash() == null ? null : Hex.decode(transactionResponse.getReferencedTransactionFullHash());
        this.senderPublicKey = Hex.decode(transactionResponse.getSenderPublicKey());
        this.signature = transactionResponse.getSignature() == null ? null : Hex.decode(transactionResponse.getSignature());
        this.signatureHash = transactionResponse.getSignatureHash() == null ? null : Hex.decode(transactionResponse.getSignatureHash());
        this.blockHeight = transactionResponse.getHeight();
        this.confirmations = transactionResponse.getConfirmations();
        this.ecBlockHeight = transactionResponse.getEcBlockHeight();
        this.subtype = transactionResponse.getSubtype();
        this.type = transactionResponse.getType();
        this.version = transactionResponse.getVersion();
        this.attachment = transactionResponse.getAttachment() == null ? new OrdinaryPaymentAttachment(transactionResponse.getVersion()) : transactionResponse.getAttachment().getAttachment().toAttachment();
        this.appendages = transactionResponse.getAttachment() == null ? new TransactionAppendix[0] : Arrays.stream(transactionResponse.getAttachment().getAppendages())
                .map(TransactionAppendixResponse::toAppendix)
                .toArray(TransactionAppendix[]::new);
        this.deadline = transactionResponse.getDeadline();
    }

    public FruitsAddress getRecipient() {
        return recipient;
    }

    public FruitsAddress getSender() {
        return sender;
    }

    public FruitsID getBlockId() {
        return blockId;
    }

    public FruitsID getEcBlockId() {
        return ecBlockId;
    }

    public FruitsID getId() {
        return id;
    }

    public FruitsTimestamp getBlockTimestamp() {
        return blockTimestamp;
    }

    public FruitsTimestamp getTimestamp() {
        return timestamp;
    }

    public FruitsValue getAmount() {
        return amount;
    }

    public FruitsValue getFee() {
        return fee;
    }

    public byte[] getFullHash() {
        return fullHash;
    }

    public byte[] getReferencedTransactionFullHash() {
        return referencedTransactionFullHash;
    }

    public byte[] getSenderPublicKey() {
        return senderPublicKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    public byte[] getSignatureHash() {
        return signatureHash;
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public int getEcBlockHeight() {
        return ecBlockHeight;
    }

    public int getSubtype() {
        return subtype;
    }

    public int getType() {
        return type;
    }

    public int getVersion() {
        return version;
    }

    public TransactionAttachment getAttachment() {
        return attachment;
    }

    public TransactionAppendix[] getAppendages() {
        return appendages;
    }

    public short getDeadline() {
        return deadline;
    }
}
