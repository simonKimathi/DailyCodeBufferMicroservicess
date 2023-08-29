package com.skytrix.paymentservice.Service;

import com.skytrix.paymentservice.Model.PaymentRequest;
import com.skytrix.paymentservice.Model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
