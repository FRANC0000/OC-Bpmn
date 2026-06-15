package com.bpmplatform.document.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.document.domain.repository.DocumentDefinitionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class AddBlocksToVersionUseCase {

    private final DocumentDefinitionRepository definitionRepository;

    public AddBlocksToVersionUseCase(DocumentDefinitionRepository definitionRepository) {
        this.definitionRepository = definitionRepository;
    }

    @Transactional
    public Output execute(Input input) {
        var definition = definitionRepository.findById(input.documentId())
                .orElseThrow(() -> new IllegalArgumentException("Document definition not found: " + input.documentId()));

        var version = definition.getVersions().stream()
                .filter(v -> v.getId().equals(input.versionId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + input.versionId()));

        version.setBlocksJson(input.blocksJson());
        definitionRepository.save(definition);

        return new Output(version.getId(), version.getVersion());
    }

    public record Input(UUID documentId, UUID versionId, String blocksJson) {}
    public record Output(UUID versionId, String version) {}
}
