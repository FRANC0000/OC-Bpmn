-- Document Engine: Block Catalogs for reusable block definitions
CREATE TABLE block_catalogs (
    id              UUID PRIMARY KEY,
    code            VARCHAR(100) NOT NULL UNIQUE,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE block_catalog_items (
    id              UUID PRIMARY KEY,
    block_catalog_id UUID NOT NULL REFERENCES block_catalogs(id),
    label           VARCHAR(255) NOT NULL,
    type            VARCHAR(50) NOT NULL,
    config_json     JSONB,
    sort_order      INTEGER NOT NULL DEFAULT 0,
    required        BOOLEAN NOT NULL DEFAULT FALSE,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_block_catalog_items_catalog ON block_catalog_items(block_catalog_id);

-- Document Engine: Metadata Schemas
CREATE TABLE metadata_schemas (
    id              UUID PRIMARY KEY,
    code            VARCHAR(100) NOT NULL UNIQUE,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    schema_json     JSONB NOT NULL,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Validate blocks_json structure constraint (ensure it's always a valid JSON array)
ALTER TABLE document_versions ADD CONSTRAINT chk_blocks_json_array
    CHECK (blocks_json IS NULL OR blocks_json = '[]' OR blocks_json::jsonb IS NOT NULL);

-- Add metadata_schema_id to document_definitions for schema binding
ALTER TABLE document_definitions ADD COLUMN IF NOT EXISTS metadata_schema_id UUID REFERENCES metadata_schemas(id);
CREATE INDEX IF NOT EXISTS idx_document_definitions_metadata_schema ON document_definitions(metadata_schema_id);

-- Process definitions: add form reference
ALTER TABLE process_definitions ADD COLUMN IF NOT EXISTS form_schema_json JSONB;
ALTER TABLE process_definitions ADD COLUMN IF NOT EXISTS metadata_schema_id UUID REFERENCES metadata_schemas(id);
CREATE INDEX IF NOT EXISTS idx_process_definitions_metadata_schema ON process_definitions(metadata_schema_id);
