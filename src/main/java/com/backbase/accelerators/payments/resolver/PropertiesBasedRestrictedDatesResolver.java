package com.backbase.accelerators.payments.resolver;

import com.backbase.accelerators.payments.config.ScheduledPaymentOrderProperties;
import com.backbase.accelerators.payments.model.RestrictedDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scheduled-payments.execution-date-validation", name = "restrictedDatesResolutionStrategy", havingValue = "PROPERTIES_BASED", matchIfMissing = true)
public class PropertiesBasedRestrictedDatesResolver implements RestrictedDatesResolver {

    private final ScheduledPaymentOrderProperties scheduledPaymentOrderProperties;

    @Override
    public List<RestrictedDate> getRestrictedDates() {
        return scheduledPaymentOrderProperties.getExecutionDateValidation()
                .getRestrictedDates();
    }
}
