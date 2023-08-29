package com.skytrix.paymentservice.Service.Impl;

import com.skytrix.paymentservice.Entity.TransactionDetails;
import com.skytrix.paymentservice.Model.PaymentMode;
import com.skytrix.paymentservice.Model.PaymentRequest;
import com.skytrix.paymentservice.Model.PaymentResponse;
import com.skytrix.paymentservice.Repository.TransactionDetailsRepository;
import com.skytrix.paymentservice.Service.PaymentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;
    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording payment Details: {}", paymentRequest);

        TransactionDetails transactionDetails
                = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();

        transactionDetailsRepository.save(transactionDetails);

        log.info("Transaction completed with id {}", transactionDetails.getId());
        return transactionDetails.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(String orderId) {
        log.info("Getting payment details for the order id: {}", orderId);

//        Optional<TransactionDetails> optionalTransactionDetails
//                = Optional.ofNullable(transactionDetailsRepository.findByOrderId(Long.parseLong(orderId)));

        TransactionDetails transactionDetails = transactionDetailsRepository.findByOrderId(Long.parseLong(orderId));

        log.info("Transaction by order id size {}", transactionDetails);

        if (transactionDetails != null) {
            PaymentResponse paymentResponse
                    = PaymentResponse.builder()
                    .paymentId(transactionDetails.getId())
                    .maymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                    .paymentDate(transactionDetails.getPaymentDate())
                    .orderId(transactionDetails.getOrderId())
                    .status(transactionDetails.getPaymentStatus())
                    .amount(transactionDetails.getAmount())
                    .build();

            log.info("Payment responce");
            return paymentResponse;
        } else {
            return new PaymentResponse();
        }
    }
}
