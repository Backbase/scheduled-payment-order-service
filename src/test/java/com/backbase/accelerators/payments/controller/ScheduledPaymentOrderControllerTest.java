package com.backbase.accelerators.payments.controller;

import com.backbase.accelerators.payments.service.ScheduledPaymentOrderService;
import com.backbase.payments.scheduled.service.model.ValidateExecutionDateResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static com.backbase.payments.scheduled.service.model.ValidateExecutionDateResponse.StatusEnum.RESTRICTED_DATE_DETECTED;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledPaymentOrderControllerTest {

    @Mock
    private ScheduledPaymentOrderService scheduledPaymentOrderService;

    @InjectMocks
    private ScheduledPaymentOrderController scheduledPaymentOrderController;

    @Test
    public void should_return_validate_execution_date_response() {
        when(scheduledPaymentOrderService.validateExecutionDate(LocalDate.parse("2022-12-25"))).thenReturn(getValidateExecutionDateResponse());
        ResponseEntity<ValidateExecutionDateResponse> result = scheduledPaymentOrderController.validateExecutionDate(LocalDate.parse("2022-12-25"));

        assertNotNull(result.getBody());
        assertEquals(LocalDate.parse("2022-12-25"), result.getBody().getOriginalExecutionDate());
        assertEquals(LocalDate.parse("2022-12-23"), result.getBody().getNextAvailableExecutionDateBefore());
        assertEquals(LocalDate.parse("2022-12-26"), result.getBody().getNextAvailableExecutionDateAfter());
        assertEquals(RESTRICTED_DATE_DETECTED, result.getBody().getStatus());
    }

    private ValidateExecutionDateResponse getValidateExecutionDateResponse() {
        return new ValidateExecutionDateResponse()
                .originalExecutionDate(LocalDate.parse("2022-12-25"))
                .nextAvailableExecutionDateBefore(LocalDate.parse("2022-12-23"))
                .nextAvailableExecutionDateAfter(LocalDate.parse("2022-12-26"))
                .status(ValidateExecutionDateResponse.StatusEnum.RESTRICTED_DATE_DETECTED);
    }


}