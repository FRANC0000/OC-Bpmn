package com.bpmplatform.process.infrastructure.persistence.jpa.converter;

import com.bpmplatform.process.domain.valueobject.ProcessStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProcessStatusConverter implements AttributeConverter<ProcessStatus, String> {

    @Override
    public String convertToDatabaseColumn(ProcessStatus attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public ProcessStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ProcessStatus.fromString(dbData);
    }
}
