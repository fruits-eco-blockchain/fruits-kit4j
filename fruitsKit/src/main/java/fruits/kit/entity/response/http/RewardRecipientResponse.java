package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public class RewardRecipientResponse extends FRSResponse {
    private final String rewardRecipient;

    public RewardRecipientResponse(String rewardRecipient) {
        this.rewardRecipient = rewardRecipient;
    }

    public String getRewardRecipient() {
        return rewardRecipient;
    }
}
