package fruits.kit.entity.response.attachment;

import java.util.Map;
import java.util.stream.Collectors;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.response.TransactionAttachment;

public class MultiOutAttachment extends TransactionAttachment {
    private final Map<FruitsAddress, FruitsValue> outputs;

    public MultiOutAttachment(int version, Map<FruitsAddress, FruitsValue> outputs) {
        super(version);
        this.outputs = outputs;
    }

    public Map<FruitsAddress, FruitsValue> getOutputs() {
        return outputs;
    }
}
