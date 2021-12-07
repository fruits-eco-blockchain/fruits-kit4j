package fruits.kit.entity.response.attachment;

import fruits.kit.entity.response.TransactionAttachment;

public class AskOrderCancellationAttachment extends TransactionAttachment {
    private String order;

    public AskOrderCancellationAttachment(int version, String order) {
        super(version);
        this.order = order;
    }
    //TODO constructor for FnsApi

    public String getOrder() {
        return order;
    }
}
