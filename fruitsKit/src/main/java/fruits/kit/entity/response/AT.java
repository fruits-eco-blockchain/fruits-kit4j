package fruits.kit.entity.response;

import org.bouncycastle.util.encoders.Hex;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.response.http.ATResponse;

public class AT {
    private final boolean dead;
    private final boolean finished;
    private final boolean frozen;
    private final boolean running;
    private final boolean stopped;
    private final FruitsAddress creator;
    private final FruitsAddress id;
    private final FruitsValue balance;
    private final FruitsValue minimumActivation;
    private final FruitsValue previousBalance;
    private final byte[] machineCode;
    private final byte[] machineData;
    private final int creationHeight;
    private final int nextBlockHeight;
    private final int version;
    private final String description;
    private final String name;

    public AT(boolean dead, boolean finished, boolean frozen, boolean running, boolean stopped, FruitsAddress creator, FruitsAddress id, FruitsValue balance, FruitsValue minimumActivation, FruitsValue previousBalance, byte[] machineCode, byte[] machineData, int creationHeight, int nextBlockHeight, int version, String description, String name) {
        this.dead = dead;
        this.finished = finished;
        this.frozen = frozen;
        this.running = running;
        this.stopped = stopped;
        this.creator = creator;
        this.id = id;
        this.balance = balance;
        this.minimumActivation = minimumActivation;
        this.previousBalance = previousBalance;
        this.machineCode = machineCode;
        this.machineData = machineData;
        this.creationHeight = creationHeight;
        this.nextBlockHeight = nextBlockHeight;
        this.version = version;
        this.description = description;
        this.name = name;
    }

    public AT(ATResponse atResponse) {
        this.dead = atResponse.isDead();
        this.finished = atResponse.isFinished();
        this.frozen = atResponse.isFrozen();
        this.running = atResponse.isRunning();
        this.stopped = atResponse.isStopped();
        this.id = FruitsAddress.fromEither(atResponse.getAt());
        this.balance = FruitsValue.fromPlanck(atResponse.getBalanceNQT());
        this.creator = atResponse.getCreator() == null ? null : FruitsAddress.fromEither(atResponse.getCreator());
        this.minimumActivation = atResponse.getMinActivation() == null ? null : FruitsValue.fromPlanck(atResponse.getMinActivation());
        this.previousBalance = FruitsValue.fromPlanck(atResponse.getPrevBalanceNQT());
        this.machineCode = atResponse.getMachineCode() == null ? null : Hex.decode(atResponse.getMachineCode());
        this.machineData = Hex.decode(atResponse.getMachineData());
        this.creationHeight = atResponse.getCreationBlock();
        this.nextBlockHeight = atResponse.getNextBlock();
        this.version = atResponse.getAtVersion();
        this.description = atResponse.getDescription();
        this.name = atResponse.getName();
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isStopped() {
        return stopped;
    }

    public FruitsAddress getCreator() {
        return creator;
    }

    public FruitsAddress getId() {
        return id;
    }

    public FruitsValue getBalance() {
        return balance;
    }

    public FruitsValue getMinimumActivation() {
        return minimumActivation;
    }

    public FruitsValue getPreviousBalance() {
        return previousBalance;
    }

    public byte[] getMachineCode() {
        return machineCode;
    }

    public byte[] getMachineData() {
        return machineData;
    }

    public int getCreationHeight() {
        return creationHeight;
    }

    public int getNextBlockHeight() {
        return nextBlockHeight;
    }

    public int getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
