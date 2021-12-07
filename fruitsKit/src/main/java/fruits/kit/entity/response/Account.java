package fruits.kit.entity.response;

import org.bouncycastle.util.encoders.Hex;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.response.http.AccountResponse;

public class Account {
    private final FruitsAddress id;
    private final FruitsValue balance;
    private final FruitsValue commitmentNQT;
    private final FruitsValue committedBalanceNQT;
    private final FruitsValue forgedBalance;
    private final FruitsValue unconfirmedBalance;
    private final byte[] publicKey;
    private final String description;
    private final String name;

    public Account(FruitsAddress id, FruitsValue balance, FruitsValue commitmentNQT, FruitsValue committedBalanceNQT, FruitsValue forgedBalance, FruitsValue unconfirmedBalance, byte[] publicKey, String description, String name) {
        this.id = id;
        this.balance = balance;
        this.commitmentNQT = commitmentNQT;
        this.committedBalanceNQT = committedBalanceNQT;
        this.forgedBalance = forgedBalance;
        this.unconfirmedBalance = unconfirmedBalance;
        this.publicKey = publicKey;
        this.description = description;
        this.name = name;
    }

    public Account(AccountResponse accountResponse) {
        this.id = FruitsAddress.fromEither(accountResponse.getAccount());
        this.balance = FruitsValue.fromPlanck(accountResponse.getBalanceNQT());
        this.commitmentNQT = FruitsValue.fromPlanck(accountResponse.getCommitmentNQT());
        this.committedBalanceNQT = FruitsValue.fromPlanck(accountResponse.getCommittedBalanceNQT());
        this.forgedBalance = FruitsValue.fromPlanck(accountResponse.getForgedBalanceNQT());
        this.unconfirmedBalance = FruitsValue.fromPlanck(accountResponse.getUnconfirmedBalanceNQT());
        this.publicKey = accountResponse.getPublicKey() == null ? new byte[32] : Hex.decode(accountResponse.getPublicKey());
        this.description = accountResponse.getDescription();
        this.name = accountResponse.getName();
    }

    public FruitsAddress getId() {
        return id;
    }

    public FruitsValue getBalance() {
        return balance;
    }
    
    public FruitsValue getCommitment() {
        return commitmentNQT;
    }

    public FruitsValue getCommittedBalance() {
        return committedBalanceNQT;
    }

    public FruitsValue getForgedBalance() {
        return forgedBalance;
    }

    public FruitsValue getUnconfirmedBalance() {
        return unconfirmedBalance;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
