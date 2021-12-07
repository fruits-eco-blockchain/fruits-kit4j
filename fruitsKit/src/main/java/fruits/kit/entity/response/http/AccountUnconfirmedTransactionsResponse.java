package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class AccountUnconfirmedTransactionsResponse extends FRSResponse {
    private final TransactionResponse[] unconfirmedTransactions;

    public AccountUnconfirmedTransactionsResponse(TransactionResponse[] unconfirmedTransactions) {
        this.unconfirmedTransactions = unconfirmedTransactions;
    }

    public TransactionResponse[] getUnconfirmedTransactions() {
        return unconfirmedTransactions;
    }
}
