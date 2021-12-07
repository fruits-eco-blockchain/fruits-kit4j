package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class AccountTransactionsResponse extends FRSResponse {
    private final TransactionResponse[] transactions;

    public AccountTransactionsResponse(TransactionResponse[] transactions) {
        this.transactions = transactions;
    }

    public TransactionResponse[] getTransactions() {
        return transactions;
    }
}
