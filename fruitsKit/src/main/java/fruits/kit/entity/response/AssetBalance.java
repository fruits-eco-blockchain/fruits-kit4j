package fruits.kit.entity.response;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsID;
import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.response.http.AssetAccountResponse;

public class AssetBalance {
    private final FruitsAddress accountAddress;
    private final FruitsID assetId;
    /**
     * Quantity of the asset owned by the account. Not actually in Fruits; The FruitsValue class is used as a utility.
     * Actually measured in terms of how many of the specific asset there are.
     */
    private final FruitsValue balance;
    /**
     * Unconfirmed quantity of the asset owned by the account. Not actually in Fruits; The FruitsValue class is used as a utility.
     * Actually measured in terms of how many of the specific asset there are.
     */
    private final FruitsValue unconfirmedBalance;

    public AssetBalance(FruitsAddress accountAddress, FruitsID assetId, FruitsValue balance, FruitsValue unconfirmedBalance) {
        this.accountAddress = accountAddress;
        this.assetId = assetId;
        this.balance = balance;
        this.unconfirmedBalance = unconfirmedBalance;
    }

    public AssetBalance(AssetAccountResponse accountResponse) {
        this.accountAddress = FruitsAddress.fromEither(accountResponse.getAccount());
        this.assetId = FruitsID.fromLong(accountResponse.getAsset());
        this.balance = FruitsValue.fromPlanck(accountResponse.getQuantityQNT());
        this.unconfirmedBalance = FruitsValue.fromPlanck(accountResponse.getUnconfirmedQuantityQNT());
    }

    public FruitsAddress getAccountAddress() {
        return accountAddress;
    }

    public FruitsID getAssetId() {
        return assetId;
    }

    public FruitsValue getBalance() {
        return balance;
    }

    public FruitsValue getUnconfirmedBalance() {
        return unconfirmedBalance;
    }
}
