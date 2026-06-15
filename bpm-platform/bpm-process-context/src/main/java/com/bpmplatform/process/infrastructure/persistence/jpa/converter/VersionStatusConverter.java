package com.bpmplatform.process.infrastructure.persistence.jpa.converter;

import com.bpmplatform.process.domain.valueobject.VersionStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VersionStatusConverter implements AttributeConverter<VersionStatus, String> {

    @Override
    public String convertToDatabaseColumn(VersionStatus attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public VersionStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : VersionStatus.fromString(dbData);
    }
}
