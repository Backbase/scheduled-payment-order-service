package com.backbase.accelerators.payments.mapper;

import com.backbase.accelerators.payments.model.entity.ScheduledPaymentOrderTransactionEntity;
import com.backbase.payments.scheduled.service.model.PostScheduledPaymentOrderTransactionRequest;
import com.backbase.payments.scheduled.service.model.ScheduledPaymentOrderTransactionItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduledPaymentOrderMapper {

    ScheduledPaymentOrderTransactionEntity toEntity(PostScheduledPaymentOrderTransactionRequest request);

    ScheduledPaymentOrderTransactionItem toScheduledPaymentOrderTransactionItem(ScheduledPaymentOrderTransactionEntity entity);
}
