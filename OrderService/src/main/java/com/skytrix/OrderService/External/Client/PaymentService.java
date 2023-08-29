package com.skytrix.OrderService.External.Client;

import com.skytrix.OrderService.Exceptions.CustomException;
import com.skytrix.OrderService.External.Request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE")
public interface PaymentService {

    @PostMapping("/payment")
    public ResponseEntity<Long> doPayment(
            @RequestBody PaymentRequest paymentRequest
    );

    default ResponseEntity<Long> fallback(Exception e){
        throw new CustomException("Payment Service not available",
                "UNAVAILABLE",
                500);

    }
}
