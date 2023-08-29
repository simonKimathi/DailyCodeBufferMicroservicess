package com.skytrix.OrderService.Service.Impl;

import com.skytrix.OrderService.Entity.Order;
import com.skytrix.OrderService.Exceptions.CustomException;
import com.skytrix.OrderService.External.Client.PaymentService;
import com.skytrix.OrderService.External.Client.ProductService;
import com.skytrix.OrderService.External.Request.PaymentRequest;
import com.skytrix.OrderService.External.responce.PaymentResponse;
import com.skytrix.OrderService.Model.OrderRequest;
import com.skytrix.OrderService.Model.OrderResponse;
import com.skytrix.OrderService.External.responce.ProductResponse;
import com.skytrix.OrderService.Repository.OrderRepository;
import com.skytrix.OrderService.Service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public long placeOrder(OrderRequest orderRequest) {

        log.info("Placing order request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating order with status CREATED");

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order = orderRepository.save(order);

        log.info("Calling Payment Service to complete the payment");

        PaymentRequest paymentrequest
                = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;

        try {
            paymentService.doPayment(paymentrequest);
            log.info("Payment done successfully. Changing order status to placed");
            orderStatus = "PLACED";
        }catch (Exception e){
            log.error("Error occured in making the payment. Changing order status to PAYMENT_FAILED");
            orderStatus="PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order placed successfully with order Id: {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for Order Id: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found for the orderId "+ orderId, "NOT_FOUND", 404));

        log.info("Invoking product service to fetch a product details of productId {}", order.getProductId());

        ProductResponse productResponse
                = restTemplate.getForObject(
                        "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class
        );

        log.info("Getting payment information from the payment Service");

        PaymentResponse paymentResponse
                = restTemplate.getForObject(
                        "http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentResponse.class
        );

//        assert productResponse != null;
        OrderResponse.ProductDetails productDetails
                = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();

//        assert paymentResponse != null;
        OrderResponse.PaymentDetails paymentDetails
                = OrderResponse.PaymentDetails
                .builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getPaymentStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .orderDate(order.getOrderDate())
                .build();
    }

}
