package com.backbase.accelerators.payments.repository;

import com.backbase.accelerators.payments.model.entity.ScheduledPaymentOrderTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledPaymentOrderTransactionRepository
        extends PagingAndSortingRepository<ScheduledPaymentOrderTransactionEntity, Long> {

    Page<ScheduledPaymentOrderTransactionEntity> findAllByScheduledPaymentOrderId(
            String scheduledPaymentOrderId,
            Pageable pageable);
}
