package fruits.kit.entity.response.attachment;

import fruits.kit.entity.response.TransactionAttachment;

public class BidOrderCancellationAttachment extends TransactionAttachment {
    private String order;

    public BidOrderCancellationAttachment(int version, String order) {
        super(version);
        this.order = order;
    }
    //TODO constructor for FnsApi

    public String getOrder() {
        return order;
    }
}
