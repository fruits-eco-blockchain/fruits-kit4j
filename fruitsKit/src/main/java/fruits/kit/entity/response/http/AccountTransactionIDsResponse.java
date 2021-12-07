package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class AccountTransactionIDsResponse extends FRSResponse {
    private final String[] transactionIds;

    public AccountTransactionIDsResponse(String[] transactionIds) {
        this.transactionIds = transactionIds;
    }

    public String[] getTransactionIds() {
        return transactionIds;
    }
}
