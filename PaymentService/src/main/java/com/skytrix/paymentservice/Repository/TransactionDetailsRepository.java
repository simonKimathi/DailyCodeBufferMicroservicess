package com.skytrix.paymentservice.Repository;

import com.skytrix.paymentservice.Entity.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {
    TransactionDetails findByOrderId(long orderId);
//    Optional<TransactionDetails> findByOrderId(long orderId);
}
