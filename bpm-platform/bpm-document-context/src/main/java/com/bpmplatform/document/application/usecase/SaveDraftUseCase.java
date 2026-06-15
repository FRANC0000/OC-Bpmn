package com.bpmplatform.document.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.document.domain.entity.DocumentInstance;
import com.bpmplatform.document.domain.repository.DocumentInstanceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class SaveDraftUseCase {

    private final DocumentInstanceRepository instanceRepository;

    public SaveDraftUseCase(DocumentInstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    @Transactional
    public Output execute(Input input) {
        var instance = instanceRepository.findById(input.instanceId())
                .orElseThrow(() -> new IllegalArgumentException("Document instance not found: " + input.instanceId()));

        instance.setValuesJson(input.valuesJson());
        instanceRepository.save(instance);

        return new Output(instance.getId(), instance.getFolio(), instance.getStatus());
    }

    public record Input(UUID instanceId, String valuesJson) {}
    public record Output(UUID instanceId, String folio, String status) {}
}
