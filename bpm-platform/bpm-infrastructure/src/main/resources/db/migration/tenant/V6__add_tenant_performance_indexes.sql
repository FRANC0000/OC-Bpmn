-- ============================================================================
-- V6: Performance Indexes, Constraints & JSONB GIN Indexes for Tenant Schema
-- ============================================================================

-- --------------------------------------------------------------------------
-- 1. PROCESS CONTEXT
-- --------------------------------------------------------------------------

-- process_definitions
CREATE INDEX IF NOT EXISTS idx_process_def_owner ON process_definitions(owner_user_id);
CREATE INDEX IF NOT EXISTS idx_process_def_status ON process_definitions(status);
CREATE INDEX IF NOT EXISTS idx_process_def_slug_status ON process_definitions(slug, status);
CREATE INDEX IF NOT EXISTS idx_process_def_updated ON process_definitions(updated_at DESC);

ALTER TABLE process_definitions DROP CONSTRAINT IF EXISTS ck_process_def_status;
ALTER TABLE process_definitions ADD CONSTRAINT ck_process_def_status
    CHECK (status IN ('draft', 'active', 'inactive'));

-- process_versions
CREATE INDEX IF NOT EXISTS idx_process_ver_status ON process_versions(status);
CREATE INDEX IF NOT EXISTS idx_process_ver_created_by ON process_versions(created_by);
CREATE INDEX IF NOT EXISTS idx_process_ver_created_at ON process_versions(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_process_ver_process_status ON process_versions(process_id, status, version);

-- Full-text search on bpmn_xml for process discovery
CREATE INDEX IF NOT EXISTS idx_process_ver_bpmn_fts
    ON process_versions USING GIN (to_tsvector('spanish', coalesce(bpmn_xml, '')));

ALTER TABLE process_versions DROP CONSTRAINT IF EXISTS ck_process_ver_status;
ALTER TABLE process_versions ADD CONSTRAINT ck_process_ver_status
    CHECK (status IN ('draft', 'published', 'deprecated'));

-- process_templates
CREATE INDEX IF NOT EXISTS idx_process_tpl_category ON process_templates(category);
CREATE INDEX IF NOT EXISTS idx_process_tpl_published ON process_templates(is_published);
CREATE INDEX IF NOT EXISTS idx_process_tpl_cat_pub ON process_templates(category, is_published);
CREATE INDEX IF NOT EXISTS idx_process_tpl_created ON process_templates(created_at DESC);


-- --------------------------------------------------------------------------
-- 2. DOCUMENT CONTEXT
-- --------------------------------------------------------------------------

-- document_definitions
CREATE INDEX IF NOT EXISTS idx_doc_def_status ON document_definitions(status);
CREATE INDEX IF NOT EXISTS idx_doc_def_updated ON document_definitions(updated_at DESC);

ALTER TABLE document_definitions DROP CONSTRAINT IF EXISTS ck_doc_def_status;
ALTER TABLE document_definitions ADD CONSTRAINT ck_doc_def_status
    CHECK (status IN ('draft', 'active', 'inactive'));

-- document_versions
CREATE INDEX IF NOT EXISTS idx_doc_ver_status ON document_versions(status);
CREATE INDEX IF NOT EXISTS idx_doc_ver_created_by ON document_versions(created_by);
CREATE INDEX IF NOT EXISTS idx_doc_ver_created_at ON document_versions(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_doc_ver_doc_status ON document_versions(document_id, status, version);

-- JSONB GIN indexes for dynamic block/metadata queries
CREATE INDEX IF NOT EXISTS idx_doc_ver_blocks_gin ON document_versions USING GIN (blocks_json);
CREATE INDEX IF NOT EXISTS idx_doc_ver_metadata_gin ON document_versions USING GIN (metadata_json);

ALTER TABLE document_versions DROP CONSTRAINT IF EXISTS ck_doc_ver_status;
ALTER TABLE document_versions ADD CONSTRAINT ck_doc_ver_status
    CHECK (status IN ('draft', 'active', 'inactive'));

-- document_instances
CREATE INDEX IF NOT EXISTS idx_doc_inst_doc_version ON document_instances(document_id, version);
CREATE INDEX IF NOT EXISTS idx_doc_inst_status ON document_instances(status);
CREATE INDEX IF NOT EXISTS idx_doc_inst_created_by ON document_instances(created_by);
CREATE INDEX IF NOT EXISTS idx_doc_inst_process ON document_instances(process_instance_id);
CREATE INDEX IF NOT EXISTS idx_doc_inst_created_at ON document_instances(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_doc_inst_status_created ON document_instances(status, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_doc_inst_completed ON document_instances(completed_at DESC);

-- JSONB GIN index for dynamic value queries
CREATE INDEX IF NOT EXISTS idx_doc_inst_values_gin ON document_instances USING GIN (values_json);

ALTER TABLE document_instances DROP CONSTRAINT IF EXISTS ck_doc_inst_status;
ALTER TABLE document_instances ADD CONSTRAINT ck_doc_inst_status
    CHECK (status IN ('draft', 'submitted', 'completed', 'cancelled'));

-- folio_sequences
-- Composite index for year-based lookups (unique by format+year)
CREATE UNIQUE INDEX IF NOT EXISTS idx_folio_seq_format_year ON folio_sequences(format, year);


-- --------------------------------------------------------------------------
-- 3. SECURITY CONTEXT
-- --------------------------------------------------------------------------

-- roles
CREATE INDEX IF NOT EXISTS idx_roles_type ON roles(role_type);
CREATE INDEX IF NOT EXISTS idx_roles_system ON roles(is_system);

ALTER TABLE roles DROP CONSTRAINT IF EXISTS ck_roles_type;
ALTER TABLE roles ADD CONSTRAINT ck_roles_type
    CHECK (role_type IN ('primary', 'secondary'));

-- users
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_users_tenant ON users(tenant_id);
CREATE INDEX IF NOT EXISTS idx_users_primary_role ON users(primary_role_id);
CREATE INDEX IF NOT EXISTS idx_users_tenant_status ON users(tenant_id, status);
CREATE INDEX IF NOT EXISTS idx_users_tenant_email ON users(tenant_id, email);

ALTER TABLE users DROP CONSTRAINT IF EXISTS ck_users_status;
ALTER TABLE users ADD CONSTRAINT ck_users_status
    CHECK (status IN ('active', 'inactive', 'locked'));

-- user_secondary_roles
-- PK (user_id, role_id) already indexed both columns


-- --------------------------------------------------------------------------
-- 4. AUDIT CONTEXT
-- --------------------------------------------------------------------------

-- audit_log
CREATE INDEX IF NOT EXISTS idx_audit_tenant ON audit_log(tenant_id);
CREATE INDEX IF NOT EXISTS idx_audit_action ON audit_log(action);
CREATE INDEX IF NOT EXISTS idx_audit_action_created ON audit_log(action, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_audit_tenant_entity ON audit_log(tenant_id, entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_tenant_created ON audit_log(tenant_id, created_at DESC);

-- JSONB GIN for audit detail queries
CREATE INDEX IF NOT EXISTS idx_audit_details_gin ON audit_log USING GIN (details_json);

ALTER TABLE audit_log DROP CONSTRAINT IF EXISTS ck_audit_action;
ALTER TABLE audit_log ADD CONSTRAINT ck_audit_action
    CHECK (action IN ('create', 'update', 'delete', 'view', 'login', 'logout',
                      'assign', 'transfer', 'approve', 'reject', 'sign', 'delegate'));

-- delegations
CREATE INDEX IF NOT EXISTS idx_deleg_from ON delegations(from_user_id);
CREATE INDEX IF NOT EXISTS idx_deleg_to ON delegations(to_user_id);
CREATE INDEX IF NOT EXISTS idx_deleg_active ON delegations(is_active);
CREATE INDEX IF NOT EXISTS idx_deleg_from_active ON delegations(from_user_id, is_active);
CREATE INDEX IF NOT EXISTS idx_deleg_to_active ON delegations(to_user_id, is_active);
CREATE INDEX IF NOT EXISTS idx_deleg_dates ON delegations(start_date, end_date);

ALTER TABLE delegations DROP CONSTRAINT IF EXISTS ck_deleg_scope;
ALTER TABLE delegations ADD CONSTRAINT ck_deleg_scope
    CHECK (scope IN ('all', 'tasks', 'documents', 'approvals'));


-- --------------------------------------------------------------------------
-- 5. CATALOG CONTEXT
-- --------------------------------------------------------------------------

-- catalogs
CREATE INDEX IF NOT EXISTS idx_catalogs_active ON catalogs(is_active);
CREATE INDEX IF NOT EXISTS idx_catalogs_level ON catalogs(level);
CREATE INDEX IF NOT EXISTS idx_catalogs_level_active ON catalogs(level, is_active);
CREATE INDEX IF NOT EXISTS idx_catalogs_code_active ON catalogs(code) WHERE is_active = TRUE;

ALTER TABLE catalogs DROP CONSTRAINT IF EXISTS ck_catalogs_level;
ALTER TABLE catalogs ADD CONSTRAINT ck_catalogs_level
    CHECK (level IN ('system', 'tenant', 'org'));

-- catalog_items
CREATE INDEX IF NOT EXISTS idx_catalog_items_active ON catalog_items(is_active);
CREATE INDEX IF NOT EXISTS idx_catalog_items_catalog_active_order
    ON catalog_items(catalog_id, is_active, sort_order);

ALTER TABLE catalog_items DROP CONSTRAINT IF EXISTS ck_catalog_items_parent;
ALTER TABLE catalog_items ADD CONSTRAINT ck_catalog_items_parent
    CHECK (parent_id IS NULL OR parent_id != id);
