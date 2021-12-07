package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class AccountATsResponse extends FRSResponse {
    private final ATResponse[] ats;

    public AccountATsResponse(ATResponse[] ats) {
        this.ats = ats;
    }

    public ATResponse[] getATs() {
        return ats;
    }
}
