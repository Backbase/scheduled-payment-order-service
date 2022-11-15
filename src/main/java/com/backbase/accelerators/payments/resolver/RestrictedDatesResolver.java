package com.backbase.accelerators.payments.resolver;

import com.backbase.accelerators.payments.model.RestrictedDate;

import java.util.List;

public interface RestrictedDatesResolver {

    List<RestrictedDate> getRestrictedDates();
}
