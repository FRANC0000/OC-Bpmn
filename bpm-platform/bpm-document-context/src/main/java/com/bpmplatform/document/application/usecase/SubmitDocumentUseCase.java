package com.bpmplatform.document.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.common.domain.DomainEventPublisher;
import com.bpmplatform.document.domain.entity.DocumentInstance;
import com.bpmplatform.document.domain.entity.FolioSequence;
import com.bpmplatform.document.domain.repository.DocumentDefinitionRepository;
import com.bpmplatform.document.domain.repository.DocumentInstanceRepository;
import com.bpmplatform.document.domain.repository.FolioSequenceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class SubmitDocumentUseCase {

    private final DocumentDefinitionRepository definitionRepository;
    private final DocumentInstanceRepository instanceRepository;
    private final FolioSequenceRepository folioSequenceRepository;
    private final DomainEventPublisher eventPublisher;

    public SubmitDocumentUseCase(DocumentDefinitionRepository definitionRepository,
                                 DocumentInstanceRepository instanceRepository,
                                 FolioSequenceRepository folioSequenceRepository,
                                 DomainEventPublisher eventPublisher) {
        this.definitionRepository = definitionRepository;
        this.instanceRepository = instanceRepository;
        this.folioSequenceRepository = folioSequenceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Output execute(Input input) {
        var definition = definitionRepository.findByCode(input.documentCode())
                .orElseThrow(() -> new IllegalArgumentException("Document definition not found: " + input.documentCode()));

        var activeVersion = definition.getVersions().stream()
                .filter(v -> "active".equals(v.getStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active version for document: " + input.documentCode()));

        var folioSeq = folioSequenceRepository.findByFormat("DOC-%d-%06d")
                .orElseGet(() -> {
                    var newSeq = new FolioSequence(UUID.randomUUID(), "DOC-%d-%06d", java.time.Year.now().getValue());
                    return folioSequenceRepository.save(newSeq);
                });

        var folio = folioSeq.nextFolio();
        folioSequenceRepository.save(folioSeq);

        var instance = new DocumentInstance(UUID.randomUUID(), definition.getId(),
                activeVersion.getVersion(), folio, activeVersion.getBlocksJson(), input.createdBy());
        instance.setValuesJson(input.valuesJson());
        instance.submit();

        instanceRepository.save(instance);
        instance.clearEvents().forEach(eventPublisher::publish);

        return new Output(instance.getId(), folio, instance.getStatus());
    }

    public record Input(String documentCode, String valuesJson, UUID createdBy) {}
    public record Output(UUID instanceId, String folio, String status) {}
}
