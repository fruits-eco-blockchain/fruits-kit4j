package fruits.kit.entity.response.attachment;

import fruits.kit.entity.response.TransactionAttachment;

// TODO this is currently the default for unsupported types
public class OrdinaryPaymentAttachment extends TransactionAttachment {
    public OrdinaryPaymentAttachment(int version) {
        super(version);
    }
}
