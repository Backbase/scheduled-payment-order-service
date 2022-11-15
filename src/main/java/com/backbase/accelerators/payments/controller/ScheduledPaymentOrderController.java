package com.backbase.accelerators.payments.controller;

import com.backbase.accelerators.payments.service.ScheduledPaymentOrderService;
import com.backbase.payments.scheduled.service.api.ScheduledPaymentOrderApi;
import com.backbase.payments.scheduled.service.model.PostScheduledPaymentOrderTransactionRequest;
import com.backbase.payments.scheduled.service.model.PostScheduledPaymentOrderTransactionResponse;
import com.backbase.payments.scheduled.service.model.ScheduledPaymentOrderTransactionItem;
import com.backbase.payments.scheduled.service.model.ValidateExecutionDateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduledPaymentOrderController implements ScheduledPaymentOrderApi {

    private final ScheduledPaymentOrderService scheduledPaymentOrderService;

    @Override
    public ResponseEntity<List<ScheduledPaymentOrderTransactionItem>> getScheduledPaymentOrderTransactions(
            String scheduledPaymentOrderId,
            Integer from,
            Integer size,
            String direction) {

        log.info("Entering getScheduledPaymentOrderTransactions() with scheduledPaymentOrderId: {}", scheduledPaymentOrderId);
        List<ScheduledPaymentOrderTransactionItem> response = scheduledPaymentOrderService.getScheduledPaymentOrderTransactions(
                scheduledPaymentOrderId,
                from,
                size,
                direction);

        log.info("Returning scheduled payment order transactions: {}", response);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PostScheduledPaymentOrderTransactionResponse> postScheduledPaymentOrderTransaction(
            PostScheduledPaymentOrderTransactionRequest request) {

        log.info("Entering postScheduledPaymentOrderTransaction() with request: {}", request);
        PostScheduledPaymentOrderTransactionResponse response = scheduledPaymentOrderService.postScheduledPaymentOrderTransaction(request);
        log.info("Returning PostScheduledPaymentOrderTransactionResponse: {}", response);

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<ValidateExecutionDateResponse> validateExecutionDate(LocalDate executionDate) {
        log.info("Entering validateExecutionDate() with date: {}", executionDate);
        ValidateExecutionDateResponse response = scheduledPaymentOrderService.validateExecutionDate(executionDate);
        log.info("Returning ValidateExecutionDateResponse: {}", response);

        return ResponseEntity.ok(response);
    }
}
