package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class AskOrdersResponse extends FRSResponse {
    private final OrderResponse[] askOrders;

    public AskOrdersResponse(OrderResponse[] askOrders) {
        this.askOrders = askOrders;
    }

    public OrderResponse[] getOrders() {
        return askOrders;
    }
}
