package com.skytrix.OrderService.Controller;

import com.skytrix.OrderService.Model.OrderRequest;
import com.skytrix.OrderService.Model.OrderResponse;
import com.skytrix.OrderService.Service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('Customer')")
    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){

        log.info("Logs{}", orderRequest);

        long orderId = orderService.placeOrder(orderRequest);
        log.info("Order Id: {}", orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails( @PathVariable long orderId) {
        log.info("\n\norder{}", orderId);
        OrderResponse orderResponse = orderService.getOrderDetails(orderId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}


//accessToken":"eyJraWQiOiJJLWpoekFDa2dwU2d3OWtkcnFmTmFDR2RIeThqSGdveEVzSHcwN1NJYUhZIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmZPMjBJLUxsN0J5MXBEb1RNS3BRVFVsZUd3R2ptVEJqbnp1RGM1MkoxbVkub2FyMTY2cGZnaTIzdGx1WVE1ZDciLCJpc3MiOiJodHRwczovL2Rldi02MzA0MjQyNi5va3RhLmNvbS9vYXV0aDIvZGVmYXVsdCIsImF1ZCI6ImFwaTovL2RlZmF1bHQiLCJpYXQiOjE2ODkxNzgyNDgsImV4cCI6MTY4OTE4MTg0OCwiY2lkIjoiMG9hYTA5NTd0dUh2dVBwN2o1ZDciLCJ1aWQiOiIwMHVhMDdmbmVieWlsVVdQZDVkNyIsInNjcCI6WyJwcm9maWxlIiwib2ZmbGluZV9hY2Nlc3MiLCJlbWFpbCIsIm9wZW5pZCJdLCJhdXRoX3RpbWUiOjE2ODkxNzgyNDcsInN1YiI6IndhbmRlcmFzaW1vaEBnbWFpbC5jb20iLCJHcm91cHMiOlsiRXZlcnlvbmUiXX0.7psu_JiSZSU_EyG4Pdfju1aME3P7SwdS0uKFfe4YHUWqa8y5u5YLHouGDI_JgmvmW4LmQZFBzOgaJFbuBDR7WSmse6_L6l3dbpjWpUqlqg4JA-2x7qXD_DEv-h2isZm3pInCREKKJAW62JUT-o7kmaWyLi1zWCX_7TJY7KFT96_wYzUCHFTXTIKnUqJoXukutZeep8_084FrUxCk24m0_gHu1BPpLLoogb7ISqkGbS2BXls5vNPjqk0vi2x0lfbsqFjNcpY5exZRO3RwKhHhKrhT30Uwg8vx0_j1xdv0HY1A3d0THnd2HyJQYzqh-l9cQLgURr4HfLXbhAZFnFSolw