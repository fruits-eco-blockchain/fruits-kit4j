package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class AccountsAssetResponse extends FRSResponse {
    private final AssetAccountResponse[] accountAssets;

    public AccountsAssetResponse(AssetAccountResponse[] accountAssets) {
        this.accountAssets = accountAssets;
    }

    public AssetAccountResponse[] getAccountsAsset() {
        return accountAssets;
    }
}
