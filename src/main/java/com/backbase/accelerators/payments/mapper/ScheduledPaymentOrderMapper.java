package com.backbase.accelerators.payments.mapper;

import com.backbase.accelerators.payments.model.entity.ScheduledPaymentOrderTransactionEntity;
import com.backbase.payments.scheduled.service.model.PostScheduledPaymentOrderTransactionRequest;
import com.backbase.payments.scheduled.service.model.ScheduledPaymentOrderTransactionItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface ScheduledPaymentOrderMapper {

    @Mapping(source = "additions", target = "additions", qualifiedByName = "toJsonString")
    ScheduledPaymentOrderTransactionEntity toEntity(PostScheduledPaymentOrderTransactionRequest request);

    @Mapping(source = "additions", target = "additions", qualifiedByName = "toMap")
    ScheduledPaymentOrderTransactionItem toScheduledPaymentOrderTransactionItem(ScheduledPaymentOrderTransactionEntity entity);

    @SneakyThrows
    @Named("toJsonString")
    default String toJsonString(Map<String, String> additions) {
        if (isNull(additions)) {
            return null;
        }

        return new ObjectMapper().writeValueAsString(additions);
    }

    @SneakyThrows
    @Named("toMap")
    default Map<String, String> toMap(String additionsJsonString) {
        if (isNull(additionsJsonString)) {
            return emptyMap();
        }

        return new ObjectMapper().readValue(additionsJsonString, Map.class);
    }
}
