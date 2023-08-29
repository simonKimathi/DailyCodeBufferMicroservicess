package com.skytrix.OrderService.Service;

import com.skytrix.OrderService.Model.OrderRequest;
import com.skytrix.OrderService.Model.OrderResponse;

public interface OrderService {

    long placeOrder(OrderRequest orderRequest);
    OrderResponse getOrderDetails(long orderId);
}
