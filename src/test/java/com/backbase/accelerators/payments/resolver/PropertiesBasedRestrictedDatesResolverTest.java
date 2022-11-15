package com.backbase.accelerators.payments.resolver;

import com.backbase.accelerators.payments.config.ScheduledPaymentOrderProperties;
import com.backbase.accelerators.payments.model.RestrictedDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesBasedRestrictedDatesResolverTest {

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ScheduledPaymentOrderProperties scheduledPaymentOrderProperties;

    @InjectMocks
    private PropertiesBasedRestrictedDatesResolver propertiesBasedRestrictedDatesResolver;

    @Test
    public void should_return_restricted_dates_from_properties() {
        when(scheduledPaymentOrderProperties.getExecutionDateValidation().getRestrictedDates()).thenReturn(getRestrictedDates());
        List<RestrictedDate> result = propertiesBasedRestrictedDatesResolver.getRestrictedDates();

        assertEquals("Christmas Day", result.get(0).getEventName());
        assertEquals(LocalDate.parse("2022-12-25"), result.get(0).getDate());
    }

    private List<RestrictedDate> getRestrictedDates() {
        RestrictedDate restrictedDate = new RestrictedDate();
        restrictedDate.setEventName("Christmas Day");
        restrictedDate.setDate(LocalDate.parse("2022-12-25"));

        return List.of(restrictedDate);
    }

}