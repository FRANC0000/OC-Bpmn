package com.bpmplatform.tenant.infrastructure.persistence.jpa.converter;

import com.bpmplatform.tenant.domain.valueobject.TenantStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TenantStatusConverter implements AttributeConverter<TenantStatus, String> {

    @Override
    public String convertToDatabaseColumn(TenantStatus attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public TenantStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TenantStatus.fromString(dbData);
    }
}
