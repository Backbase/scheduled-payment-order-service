package com.backbase.accelerators.payments.config;

import com.backbase.accelerators.payments.constants.WeekendExecutionStrategy;
import com.backbase.accelerators.payments.model.RestrictedDate;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("scheduled-payments")
public class ScheduledPaymentOrderProperties {

    private ExecutionDateValidationProperties executionDateValidation;

    @Data
    public static class ExecutionDateValidationProperties {

        private WeekendExecutionStrategy weekendExecutionStrategy;
        private List<RestrictedDate> restrictedDates = new ArrayList<>();
    }
}
