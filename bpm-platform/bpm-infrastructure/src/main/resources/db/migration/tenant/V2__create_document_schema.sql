CREATE TABLE document_definitions (
    id              UUID PRIMARY KEY,
    code            VARCHAR(100) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    status          VARCHAR(20) NOT NULL DEFAULT 'draft',
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(code)
);

CREATE TABLE document_versions (
    id              UUID PRIMARY KEY,
    document_id     UUID NOT NULL REFERENCES document_definitions(id),
    version         VARCHAR(20) NOT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'draft',
    blocks_json     JSONB NOT NULL DEFAULT '[]',
    metadata_json   JSONB NOT NULL DEFAULT '[]',
    created_by      UUID,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(document_id, version)
);

CREATE TABLE document_instances (
    id              UUID PRIMARY KEY,
    document_id     UUID NOT NULL,
    version         VARCHAR(20) NOT NULL,
    folio           VARCHAR(100) NOT NULL UNIQUE,
    status          VARCHAR(20) NOT NULL DEFAULT 'draft',
    values_json     JSONB NOT NULL DEFAULT '{}',
    snapshot_json   JSONB NOT NULL,
    process_instance_id VARCHAR(255),
    created_by      UUID,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    completed_at    TIMESTAMP WITH TIME ZONE
);

CREATE TABLE folio_sequences (
    id              UUID PRIMARY KEY,
    format          VARCHAR(255) NOT NULL UNIQUE,
    current_seq     BIGINT NOT NULL DEFAULT 0,
    year            INTEGER NOT NULL DEFAULT EXTRACT(YEAR FROM NOW())
);
