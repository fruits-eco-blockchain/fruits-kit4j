package fruits.kit.entity.response;

import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.response.http.SuggestFeeResponse;

public class FeeSuggestion {
    private final FruitsValue cheapFee;
    private final FruitsValue standardFee;
    private final FruitsValue priorityFee;

    public FeeSuggestion(FruitsValue cheapFee, FruitsValue standardFee, FruitsValue priorityFee) {
        this.cheapFee = cheapFee;
        this.standardFee = standardFee;
        this.priorityFee = priorityFee;
    }

    public FeeSuggestion(SuggestFeeResponse suggestFeeResponse) {
        this.cheapFee = FruitsValue.fromPlanck(suggestFeeResponse.getCheap());
        this.standardFee = FruitsValue.fromPlanck(suggestFeeResponse.getStandard());
        this.priorityFee = FruitsValue.fromPlanck(suggestFeeResponse.getPriority());
    }

    public FruitsValue getCheapFee() {
        return cheapFee;
    }

    public FruitsValue getStandardFee() {
        return standardFee;
    }

    public FruitsValue getPriorityFee() {
        return priorityFee;
    }
}
