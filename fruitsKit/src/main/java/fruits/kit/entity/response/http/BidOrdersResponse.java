package fruits.kit.entity.response.http;

@SuppressWarnings("unused")
public final class BidOrdersResponse extends FRSResponse {
    private final OrderResponse[] bidOrders;

    public BidOrdersResponse(OrderResponse[] bidOrders) {
        this.bidOrders = bidOrders;
    }

    public OrderResponse[] getOrders() {
        return bidOrders;
    }
}
