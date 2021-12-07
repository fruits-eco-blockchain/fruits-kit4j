package fruits.kit.entity.response.http.attachment;

import fruits.kit.entity.response.TransactionAttachment;
import fruits.kit.entity.response.attachment.OrdinaryPaymentAttachment;

@SuppressWarnings("WeakerAccess")
public final class OrdinaryPaymentAttachmentResponse extends TransactionAttachmentResponse {
    OrdinaryPaymentAttachmentResponse() {}

    @Override
    public TransactionAttachment toAttachment() {
        return new OrdinaryPaymentAttachment(1);
    }
}
