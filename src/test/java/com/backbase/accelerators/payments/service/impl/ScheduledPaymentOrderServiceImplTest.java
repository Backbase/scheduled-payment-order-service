package com.backbase.accelerators.payments.service.impl;

import com.backbase.accelerators.payments.config.ScheduledPaymentOrderProperties;
import com.backbase.accelerators.payments.mapper.ScheduledPaymentOrderMapper;
import com.backbase.accelerators.payments.model.RestrictedDate;
import com.backbase.accelerators.payments.repository.ScheduledPaymentOrderTransactionRepository;
import com.backbase.accelerators.payments.resolver.RestrictedDatesResolver;
import com.backbase.payments.scheduled.service.model.ValidateExecutionDateResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;

import static com.backbase.accelerators.payments.constants.WeekendExecutionStrategy.ALLOW;
import static com.backbase.accelerators.payments.constants.WeekendExecutionStrategy.RESCHEDULE;
import static com.backbase.payments.scheduled.service.model.ValidateExecutionDateResponse.StatusEnum.OK;
import static com.backbase.payments.scheduled.service.model.ValidateExecutionDateResponse.StatusEnum.RESTRICTED_DATE_DETECTED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledPaymentOrderServiceImplTest {

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ScheduledPaymentOrderProperties scheduledPaymentOrderProperties;

    @Mock
    private RestrictedDatesResolver restrictedDatesResolver;

    @Mock
    private ScheduledPaymentOrderTransactionRepository scheduledPaymentOrderTransactionRepository;

    @Spy
    private ScheduledPaymentOrderMapper scheduledPaymentOrderMapper;

    @InjectMocks
    private ScheduledPaymentOrderServiceImpl scheduledPaymentOrderService;

    @Test
    public void should_return_alternative_dates_when_execution_date_falls_on_holiday() {
        when(scheduledPaymentOrderProperties.getExecutionDateValidation().getWeekendExecutionStrategy()).thenReturn(RESCHEDULE);
        when(restrictedDatesResolver.getRestrictedDates()).thenReturn(getRestrictedDates());

        // 2022-12-25 falls on a Sunday
        ValidateExecutionDateResponse result = scheduledPaymentOrderService.validateExecutionDate(LocalDate.parse("2022-12-25"));
        System.out.println(result);

        assertEquals(LocalDate.parse("2022-12-25"), result.getOriginalExecutionDate());
        assertEquals(LocalDate.parse("2022-12-23"), result.getNextAvailableExecutionDateBefore());
        assertEquals(LocalDate.parse("2022-12-26"), result.getNextAvailableExecutionDateAfter());
        assertEquals(RESTRICTED_DATE_DETECTED, result.getStatus());
    }

    @Test
    public void should_return_alternative_dates_when_execution_date_falls_on_a_weekend() {
        when(scheduledPaymentOrderProperties.getExecutionDateValidation().getWeekendExecutionStrategy()).thenReturn(RESCHEDULE);
        when(restrictedDatesResolver.getRestrictedDates()).thenReturn(getRestrictedDates());

        // 2022-12-24 falls on a Saturday
        ValidateExecutionDateResponse result = scheduledPaymentOrderService.validateExecutionDate(LocalDate.parse("2022-12-24"));
        System.out.println(result);

        assertEquals(LocalDate.parse("2022-12-24"), result.getOriginalExecutionDate());
        assertEquals(LocalDate.parse("2022-12-23"), result.getNextAvailableExecutionDateBefore());
        assertEquals(LocalDate.parse("2022-12-26"), result.getNextAvailableExecutionDateAfter());
        assertEquals(RESTRICTED_DATE_DETECTED, result.getStatus());
    }

    @Test
    public void should_only_return_original_execution_date_due_to_valid_date() {
        when(scheduledPaymentOrderProperties.getExecutionDateValidation().getWeekendExecutionStrategy()).thenReturn(RESCHEDULE);
        when(restrictedDatesResolver.getRestrictedDates()).thenReturn(getRestrictedDates());

        // 2022-12-23 falls on a Friday
        ValidateExecutionDateResponse result = scheduledPaymentOrderService.validateExecutionDate(LocalDate.parse("2022-12-23"));
        System.out.println(result);

        assertEquals(LocalDate.parse("2022-12-23"), result.getOriginalExecutionDate());
        assertNull(result.getNextAvailableExecutionDateBefore());
        assertNull(result.getNextAvailableExecutionDateAfter());
        assertEquals(OK, result.getStatus());
    }

    @Test
    public void should_only_return_original_execution_date_due_when_weekend_execution_permitted() {
        // Allow weekend execution
        when(scheduledPaymentOrderProperties.getExecutionDateValidation().getWeekendExecutionStrategy()).thenReturn(ALLOW);
        when(restrictedDatesResolver.getRestrictedDates()).thenReturn(getRestrictedDates());

        // 2022-12-24 falls on a Saturday
        ValidateExecutionDateResponse result = scheduledPaymentOrderService.validateExecutionDate(LocalDate.parse("2022-12-24"));
        System.out.println(result);

        assertEquals(LocalDate.parse("2022-12-24"), result.getOriginalExecutionDate());
        assertNull(result.getNextAvailableExecutionDateBefore());
        assertNull(result.getNextAvailableExecutionDateAfter());
        assertEquals(OK, result.getStatus());
    }

    private List<RestrictedDate> getRestrictedDates() {
        RestrictedDate restrictedDate = new RestrictedDate();
        restrictedDate.setEventName("Christmas Day");
        restrictedDate.setDate(LocalDate.parse("2022-12-25"));

        return List.of(restrictedDate);
    }

}