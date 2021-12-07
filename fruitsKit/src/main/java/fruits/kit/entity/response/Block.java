package fruits.kit.entity.response;

import org.bouncycastle.util.encoders.Hex;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsID;
import fruits.kit.entity.FruitsTimestamp;
import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.response.http.BlockResponse;

import java.math.BigInteger;
import java.util.Arrays;

public class Block {
    private final BigInteger nonce;
    private final FruitsAddress generator;
    private final FruitsID id;
    private final FruitsID nextBlock;
    private final FruitsID previousBlock;
    private final FruitsID[] transactions;
    private final FruitsTimestamp timestamp;
    private final FruitsValue blockReward;
    private final FruitsValue totalAmount;
    private final FruitsValue totalFee;
    private final byte[] generationSignature;
    private final byte[] generatorPublicKey;
    private final byte[] payloadHash;
    private final byte[] previousBlockHash;
    private final byte[] signature;
    private final int height;
    private final int payloadLength;
    private final int scoopNum;
    private final int version;
    private final long baseTarget;
    private final long averageCommitmentNQT;

    public Block(BigInteger nonce, FruitsAddress generator, FruitsID id, FruitsID nextBlock, FruitsID previousBlock, FruitsID[] transactions, FruitsTimestamp timestamp, FruitsValue blockReward, FruitsValue totalAmount, FruitsValue totalFee, byte[] generationSignature, byte[] generatorPublicKey, byte[] payloadHash, byte[] previousBlockHash, byte[] signature, int height, int payloadLength, int scoopNum, int version, long baseTarget, long averageCommitmentNQT) {
        this.nonce = nonce;
        this.generator = generator;
        this.id = id;
        this.nextBlock = nextBlock;
        this.previousBlock = previousBlock;
        this.transactions = transactions;
        this.timestamp = timestamp;
        this.blockReward = blockReward;
        this.totalAmount = totalAmount;
        this.totalFee = totalFee;
        this.generationSignature = generationSignature;
        this.generatorPublicKey = generatorPublicKey;
        this.payloadHash = payloadHash;
        this.previousBlockHash = previousBlockHash;
        this.signature = signature;
        this.height = height;
        this.payloadLength = payloadLength;
        this.scoopNum = scoopNum;
        this.version = version;
        this.baseTarget = baseTarget;
        this.averageCommitmentNQT = averageCommitmentNQT;
    }

    public Block(BlockResponse blockResponse) {
        this.nonce = new BigInteger(blockResponse.getNonce());
        this.generator = FruitsAddress.fromEither(blockResponse.getGenerator());
        this.id = FruitsID.fromLong(blockResponse.getBlock());
        this.nextBlock = FruitsID.fromLong(blockResponse.getNextBlock());
        this.previousBlock = FruitsID.fromLong(blockResponse.getPreviousBlock());
        this.transactions = Arrays.stream(blockResponse.getTransactions())
                .map(FruitsID::fromLong)
                .toArray(FruitsID[]::new);
        this.timestamp = FruitsTimestamp.fromFruitsTimestamp(blockResponse.getTimestamp());
        this.blockReward = FruitsValue.fromFruits(blockResponse.getBlockReward());
        this.totalAmount = FruitsValue.fromPlanck(blockResponse.getTotalAmountNQT());
        this.totalFee = FruitsValue.fromPlanck(blockResponse.getTotalFeeNQT());
        this.generationSignature = Hex.decode(blockResponse.getGenerationSignature());
        this.generatorPublicKey = Hex.decode(blockResponse.getGeneratorPublicKey());
        this.payloadHash = Hex.decode(blockResponse.getPayloadHash());
        this.previousBlockHash = Hex.decode(blockResponse.getPreviousBlockHash());
        this.signature = Hex.decode(blockResponse.getBlockSignature());
        this.height = blockResponse.getHeight();
        this.payloadLength = blockResponse.getPayloadLength();
        this.scoopNum = blockResponse.getScoopNum();
        this.version = blockResponse.getVersion();
        this.baseTarget = blockResponse.getBaseTarget();
        this.averageCommitmentNQT = blockResponse.getAverageCommitmentNQT();
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public FruitsAddress getGenerator() {
        return generator;
    }

    public FruitsID getId() {
        return id;
    }

    public FruitsID getNextBlock() {
        return nextBlock;
    }

    public FruitsID getPreviousBlock() {
        return previousBlock;
    }

    public FruitsID[] getTransactions() {
        return transactions;
    }

    public FruitsTimestamp getTimestamp() {
        return timestamp;
    }

    public FruitsValue getBlockReward() {
        return blockReward;
    }

    public FruitsValue getTotalAmount() {
        return totalAmount;
    }

    public FruitsValue getTotalFee() {
        return totalFee;
    }

    public byte[] getGenerationSignature() {
        return generationSignature;
    }

    public byte[] getGeneratorPublicKey() {
        return generatorPublicKey;
    }

    public byte[] getPayloadHash() {
        return payloadHash;
    }

    public byte[] getPreviousBlockHash() {
        return previousBlockHash;
    }

    public byte[] getSignature() {
        return signature;
    }

    public int getHeight() {
        return height;
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    public int getScoopNum() {
        return scoopNum;
    }

    public int getVersion() {
        return version;
    }

    public long getBaseTarget() {
        return baseTarget;
    }
    
    public long getAverageCommitmentNQT() {
		return averageCommitmentNQT;
	}
}
