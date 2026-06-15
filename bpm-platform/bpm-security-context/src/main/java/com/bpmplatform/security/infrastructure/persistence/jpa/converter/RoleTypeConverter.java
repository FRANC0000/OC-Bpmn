package com.bpmplatform.security.infrastructure.persistence.jpa.converter;

import com.bpmplatform.security.domain.valueobject.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

    @Override
    public String convertToDatabaseColumn(RoleType attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public RoleType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : RoleType.fromString(dbData);
    }
}
