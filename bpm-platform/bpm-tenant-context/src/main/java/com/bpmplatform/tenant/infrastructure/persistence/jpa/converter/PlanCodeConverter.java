package com.bpmplatform.tenant.infrastructure.persistence.jpa.converter;

import com.bpmplatform.tenant.domain.valueobject.PlanCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PlanCodeConverter implements AttributeConverter<PlanCode, String> {

    @Override
    public String convertToDatabaseColumn(PlanCode attribute) {
        return attribute == null ? null : attribute.code();
    }

    @Override
    public PlanCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : PlanCode.fromString(dbData);
    }
}
