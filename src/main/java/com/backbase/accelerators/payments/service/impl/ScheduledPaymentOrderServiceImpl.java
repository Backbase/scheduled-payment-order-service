package com.backbase.accelerators.payments.service.impl;

import com.backbase.accelerators.payments.config.ScheduledPaymentOrderProperties;
import com.backbase.accelerators.payments.constants.DateDirection;
import com.backbase.accelerators.payments.mapper.ScheduledPaymentOrderMapper;
import com.backbase.accelerators.payments.model.RestrictedDate;
import com.backbase.accelerators.payments.model.entity.ScheduledPaymentOrderTransactionEntity;
import com.backbase.accelerators.payments.repository.ScheduledPaymentOrderTransactionRepository;
import com.backbase.accelerators.payments.resolver.RestrictedDatesResolver;
import com.backbase.accelerators.payments.service.ScheduledPaymentOrderService;
import com.backbase.payments.scheduled.service.model.PostScheduledPaymentOrderTransactionRequest;
import com.backbase.payments.scheduled.service.model.PostScheduledPaymentOrderTransactionResponse;
import com.backbase.payments.scheduled.service.model.ScheduledPaymentOrderTransactionItem;
import com.backbase.payments.scheduled.service.model.ValidateExecutionDateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.backbase.accelerators.payments.constants.DateDirection.AFTER;
import static com.backbase.accelerators.payments.constants.DateDirection.BEFORE;
import static com.backbase.accelerators.payments.constants.WeekendExecutionStrategy.ALLOW;
import static com.backbase.payments.scheduled.service.model.ValidateExecutionDateResponse.StatusEnum.OK;
import static com.backbase.payments.scheduled.service.model.ValidateExecutionDateResponse.StatusEnum.RESTRICTED_DATE_DETECTED;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledPaymentOrderServiceImpl implements ScheduledPaymentOrderService {

    private final ScheduledPaymentOrderProperties scheduledPaymentOrderProperties;
    private final RestrictedDatesResolver restrictedDatesResolver;
    private final ScheduledPaymentOrderTransactionRepository scheduledPaymentOrderTransactionRepository;
    private final ScheduledPaymentOrderMapper scheduledPaymentOrderMapper;

    @Override
    public List<ScheduledPaymentOrderTransactionItem> getScheduledPaymentOrderTransactions(
            String scheduledPaymentOrderId,
            Integer from,
            Integer size,
            String direction) {

        Pageable pageable = PageRequest.of(from, size, getSort(direction));
        Page<ScheduledPaymentOrderTransactionEntity> result = scheduledPaymentOrderTransactionRepository.findAllByScheduledPaymentOrderId(
                scheduledPaymentOrderId,
                pageable);

        return result.get()
                .map(scheduledPaymentOrderMapper::toScheduledPaymentOrderTransactionItem)
                .collect(Collectors.toList());
    }

    @Override
    public PostScheduledPaymentOrderTransactionResponse postScheduledPaymentOrderTransaction(
            PostScheduledPaymentOrderTransactionRequest request) {

        ScheduledPaymentOrderTransactionEntity response = scheduledPaymentOrderTransactionRepository.save(
                scheduledPaymentOrderMapper.toEntity(request));

        log.info("Persisted scheduled payment order transaction record: {}", response);
        return new PostScheduledPaymentOrderTransactionResponse()
                .id(String.valueOf(response.getId()));
    }

    @Override
    public ValidateExecutionDateResponse validateExecutionDate(LocalDate executionDate) {

        if (isValidBusinessDate(executionDate)) {
            log.info("Execution date {} is valid and does not fall on any configured restricted date", executionDate);
            return new ValidateExecutionDateResponse()
                    .originalExecutionDate(executionDate)
                    .status(OK);
        } else {
            log.info("Adjusting execution date {} as it falls on a restricted date or weekend.", executionDate);
            return createValidateExecutionDateResponseWithAlternativeDates(executionDate);
        }
    }

    private ValidateExecutionDateResponse createValidateExecutionDateResponseWithAlternativeDates(
            LocalDate originalExecutionDate) {

        return new ValidateExecutionDateResponse()
                .originalExecutionDate(originalExecutionDate)
                .nextAvailableExecutionDateBefore(getNextAvailableDate(originalExecutionDate, BEFORE))
                .nextAvailableExecutionDateAfter(getNextAvailableDate(originalExecutionDate, AFTER))
                .status(RESTRICTED_DATE_DETECTED);
    }

    private boolean isValidBusinessDate(LocalDate executionDate) {
        return (!isRestrictedDate(executionDate) && allowWeekendExecution())
                || (!isRestrictedDate(executionDate) && !isWeekend(executionDate)
                || (isWeekend(executionDate) && allowWeekendExecution()));

    }

    private LocalDate getNextAvailableDate(LocalDate originalExecutionDate, DateDirection dateDirection) {
        if (dateDirection == BEFORE) {
            LocalDate before = originalExecutionDate.minusDays(1);

            // Additional check in case the adjusted execution date falls on a weekend
            if (isWeekend(before) && isSaturday(before) && !allowWeekendExecution()) {
                return before.minusDays(1); // Fall back 1 day to get to Friday
            } else if (isWeekend(before) && isSunday(before) && !allowWeekendExecution()) {
                return before.minusDays(2); // Fall back 2 days to get to Friday
            }

            /* If the 'before' date happens to be in the past, then return null.
            Scheduler can't do much with a date that has already gone. */
            if (isInThePast(before)) {
                return null;
            }

            return before;
        } else {
            LocalDate after = originalExecutionDate.plusDays(1);

            // Additional check in case the adjusted execution date falls on a weekend
            if (isWeekend(after) && isSaturday(after) && !allowWeekendExecution()) {
                return after.plusDays(2); // Fast-forward 2 days to get to Monday
            } else if (isWeekend(after) && isSunday(after) && !allowWeekendExecution()) {
                return after.plusDays(1); // Fast-forward 1 day to get to Monday
            }

            return after;
        }
    }

    private boolean isInThePast(LocalDate localDate) {
        return localDate.isBefore(LocalDate.now());
    }

    private boolean isRestrictedDate(LocalDate executionDate) {
        log.info("Checking if execution date {} falls on the following restricted dates: {}",
                executionDate,
                restrictedDatesResolver.getRestrictedDates());

        return restrictedDatesResolver.getRestrictedDates()
                .parallelStream()
                .map(RestrictedDate::getDate)
                .anyMatch(restrictedDate -> restrictedDate.equals(executionDate));
    }

    private boolean allowWeekendExecution() {
        return scheduledPaymentOrderProperties.getExecutionDateValidation()
                .getWeekendExecutionStrategy() == ALLOW;
    }

    private boolean isWeekend(LocalDate executionDate) {
        return executionDate.getDayOfWeek() == SATURDAY || executionDate.getDayOfWeek() == SUNDAY;
    }

    private boolean isSaturday(LocalDate executionDate) {
        return executionDate.getDayOfWeek() == SATURDAY;
    }

    private boolean isSunday(LocalDate executionDate) {
        return executionDate.getDayOfWeek() == SUNDAY;
    }

    private Sort getSort(String direction) {
        log.info("Apply sort direction of: {}", direction);
        Sort sort = Sort.by("executionDate");

        if (direction.equalsIgnoreCase("ASC")) {
            sort.ascending();
        }  else {
            sort.descending();
        }

        return sort;
    }
}
