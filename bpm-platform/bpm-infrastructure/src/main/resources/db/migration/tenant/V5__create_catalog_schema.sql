CREATE TABLE catalogs (
    id              UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    code            VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT,
    level           VARCHAR(20) NOT NULL DEFAULT 'tenant',
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE catalog_items (
    id              UUID PRIMARY KEY,
    catalog_id      UUID NOT NULL REFERENCES catalogs(id),
    parent_id       UUID REFERENCES catalog_items(id),
    code            VARCHAR(100) NOT NULL,
    label           VARCHAR(255) NOT NULL,
    sort_order      INTEGER NOT NULL DEFAULT 0,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    metadata_json   JSONB,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(catalog_id, code)
);

CREATE INDEX idx_catalog_items_parent ON catalog_items(parent_id);
CREATE INDEX idx_catalog_items_catalog ON catalog_items(catalog_id);
