package fruits.kit.entity.response;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsID;
import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.response.http.AssetResponse;

public class Asset {
    private final FruitsID accountId;
    private final FruitsAddress assetAddress;
    private final String name;
    private final int decimals;
    /**
     * Quantity of the asset. Not actually in Fruits; The FruitsValue class is used as a utility.
     */
    private final FruitsValue quantity;
    private final FruitsID assetId;
    private int numberOfTrades;
    private int numberOfTransfers;
    private int numberOfAccounts;

    public Asset(FruitsID accountId, FruitsAddress assetAddress, String name, int decimals, FruitsValue quantity, FruitsID assetId, int numberOfTrades, int numberOfTransfers, int numberOfAccounts) {
        this.accountId = accountId;
        this.assetAddress = assetAddress;
        this.name = name;
        this.decimals = decimals;
        this.quantity = quantity;
        this.assetId = assetId;
        this.numberOfTrades = numberOfTrades;
        this.numberOfTransfers = numberOfTransfers;
        this.numberOfAccounts = numberOfAccounts;
    }

    public Asset(AssetResponse assetResponse) {
        this.accountId = FruitsID.fromLong(assetResponse.getAccount());
        this.assetAddress = FruitsAddress.fromRs(assetResponse.getAccountRS());
        this.name = assetResponse.getName();
        this.decimals = assetResponse.getDecimals();
        this.quantity = FruitsValue.fromPlanck(assetResponse.getQuantityQNT());
        this.assetId = FruitsID.fromLong(assetResponse.getAsset());
        this.numberOfTrades = assetResponse.getNumberOfTrades();
        this.numberOfTransfers = assetResponse.getNumberOfTrades();
        this.numberOfAccounts = assetResponse.getNumberOfAccounts();
    }

    public FruitsID getAccountId() {
        return accountId;
    }

    public FruitsAddress getAssetAddress() {
        return assetAddress;
    }

    public String getName() {
        return name;
    }

    public int getDecimals() {
        return decimals;
    }

    public FruitsValue getQuantity() {
        return quantity;
    }

    public FruitsID getAssetId() {
        return assetId;
    }

    public int getNumberOfTrades() {
        return numberOfTrades;
    }

    public int getNumberOfTransfers() {
        return numberOfTransfers;
    }

    public int getNumberOfAccounts() {
        return numberOfAccounts;
    }
}
