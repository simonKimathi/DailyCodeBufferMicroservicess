package com.skytrix.OrderService.External.Client;

import com.skytrix.OrderService.Exceptions.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductService {

    @PutMapping("/product/reduceQuantity/{id}")
    ResponseEntity<Void> reduceQuantity(
            @PathVariable("id") long productId,
            @RequestParam long quantity
    );

    default ResponseEntity<Void> fallback(Exception e){
        throw new CustomException("Product Service not available",
                "UNAVAILABLE",
                500);

    }
}