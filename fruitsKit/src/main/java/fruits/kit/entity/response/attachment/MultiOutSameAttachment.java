package fruits.kit.entity.response.attachment;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.response.TransactionAttachment;

public class MultiOutSameAttachment extends TransactionAttachment {
    private FruitsAddress[] recipients;

    public MultiOutSameAttachment(int version, FruitsAddress[] recipients) {
        super(version);
        this.recipients = recipients;
    }

    public FruitsAddress[] getRecipients() {
        return recipients;
    }
}
