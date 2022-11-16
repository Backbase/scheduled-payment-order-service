package com.backbase.accelerators.payments.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "sch_pmt_txn")
public class ScheduledPaymentOrderTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sch_pmt_order_id")
    private String scheduledPaymentOrderId;

    @Size(max = 64)
    @Column(name = "bank_reference_id")
    private String bankReferenceId;

    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "status")
    private String status;

    @Column(name = "amount")
    private Double amount;

    @NotNull
    @Column(name = "execution_date")
    private LocalDate executionDate;

    @Size(max = 50)
    @Column(name = "reason_code")
    private String reasonCode;

    @Size(max = 105)
    @Column(name = "reason_text")
    private String reasonText;

    @Size(max = 105)
    @Column(name = "error_description")
    private String errorDescription;

    @Size(max = 500)
    @Column(name = "additions")
    private String additions;
}
