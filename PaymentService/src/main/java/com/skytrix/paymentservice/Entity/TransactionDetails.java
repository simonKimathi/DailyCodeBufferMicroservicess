package com.skytrix.paymentservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "transaction_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private long orderId;

    @Column
    private String paymentMode;

    @Column
    private String referenceNumber;

    @Column
    private Instant paymentDate;

    @Column
    private String paymentStatus;

    @Column
    private long amount;

}
