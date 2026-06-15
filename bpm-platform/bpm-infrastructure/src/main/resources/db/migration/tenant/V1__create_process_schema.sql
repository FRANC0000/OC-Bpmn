CREATE TABLE process_definitions (
    id              UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    slug            VARCHAR(100) NOT NULL UNIQUE,
    owner_user_id   UUID,
    status          VARCHAR(20) NOT NULL DEFAULT 'draft',
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE process_versions (
    id              UUID PRIMARY KEY,
    process_id      UUID NOT NULL REFERENCES process_definitions(id),
    version         VARCHAR(20) NOT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'draft',
    bpmn_xml        TEXT NOT NULL,
    comment         TEXT,
    created_by      UUID,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(process_id, version)
);

CREATE TABLE process_templates (
    id              UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    category        VARCHAR(100),
    description     TEXT,
    bpmn_xml        TEXT NOT NULL,
    config_json     JSONB,
    version         VARCHAR(20) NOT NULL DEFAULT '1.0.0',
    is_published    BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
