package com.backbase.accelerators.payments.service;

import com.backbase.payments.scheduled.service.model.PostScheduledPaymentOrderTransactionRequest;
import com.backbase.payments.scheduled.service.model.PostScheduledPaymentOrderTransactionResponse;
import com.backbase.payments.scheduled.service.model.ScheduledPaymentOrderTransactionItem;
import com.backbase.payments.scheduled.service.model.ValidateExecutionDateResponse;

import java.time.LocalDate;
import java.util.List;

public interface ScheduledPaymentOrderService {

    List<ScheduledPaymentOrderTransactionItem> getScheduledPaymentOrderTransactions(
            String scheduledPaymentOrderId,
            Integer from,
            Integer size,
            String direction);

    PostScheduledPaymentOrderTransactionResponse postScheduledPaymentOrderTransaction(
            PostScheduledPaymentOrderTransactionRequest request);

    ValidateExecutionDateResponse validateExecutionDate(LocalDate executionDate);
}
