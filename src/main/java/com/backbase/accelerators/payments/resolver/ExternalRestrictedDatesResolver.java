package com.backbase.accelerators.payments.resolver;

import com.backbase.accelerators.payments.config.ScheduledPaymentOrderProperties;
import com.backbase.accelerators.payments.model.RestrictedDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scheduled-payments.execution-date-validation", name = "restrictedDatesResolutionStrategy", havingValue = "EXTERNAL", matchIfMissing = false)
public class ExternalRestrictedDatesResolver implements RestrictedDatesResolver {

    private final ScheduledPaymentOrderProperties scheduledPaymentOrderProperties;

    @Override
    public List<RestrictedDate> getRestrictedDates() {
        // Add implementation here. You can fetch the restricted dates from a DB, API, or wherever.
        return Collections.emptyList();
    }
}
