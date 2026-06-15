-- ============================================================================
-- V3: Performance Indexes, Constraints & Tenant Resolution for Shared Schema
-- ============================================================================

-- --------------------------------------------------------------------------
-- 1. Missing Indexes for tenant_registry
-- --------------------------------------------------------------------------
CREATE INDEX IF NOT EXISTS idx_tenant_registry_plan ON tenant_registry(plan);
CREATE INDEX IF NOT EXISTS idx_tenant_registry_schema_name ON tenant_registry(schema_name);
CREATE INDEX IF NOT EXISTS idx_tenant_registry_status_plan ON tenant_registry(status, plan);

-- --------------------------------------------------------------------------
-- 2. Missing Indexes for plans
-- --------------------------------------------------------------------------
CREATE INDEX IF NOT EXISTS idx_plans_is_active ON plans(is_active);
CREATE INDEX IF NOT EXISTS idx_plans_code_active ON plans(code) WHERE is_active = TRUE;

-- --------------------------------------------------------------------------
-- 3. Check Constraints
-- --------------------------------------------------------------------------
ALTER TABLE tenant_registry DROP CONSTRAINT IF EXISTS ck_tenant_registry_status;
ALTER TABLE tenant_registry ADD CONSTRAINT ck_tenant_registry_status
    CHECK (status IN ('active', 'suspended', 'inactive'));

ALTER TABLE tenant_registry DROP CONSTRAINT IF EXISTS ck_tenant_registry_plan;
ALTER TABLE tenant_registry ADD CONSTRAINT ck_tenant_registry_plan
    CHECK (plan IN ('basic', 'professional', 'enterprise'));

ALTER TABLE plans DROP CONSTRAINT IF EXISTS ck_plans_currency;
ALTER TABLE plans ADD CONSTRAINT ck_plans_currency
    CHECK (currency IN ('USD', 'EUR', 'ARS', 'BRL', 'MXN'));

-- --------------------------------------------------------------------------
-- 4. Tenant Resolution Function
--    Efficiently resolves tenant schema from slug or header value.
--    Used by the multi-tenancy infrastructure at startup and request time.
-- --------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION resolve_tenant_schema(p_identifier VARCHAR)
RETURNS VARCHAR
LANGUAGE SQL
STABLE
PARALLEL SAFE
RETURN (
    SELECT schema_name
    FROM tenant_registry
    WHERE slug = p_identifier OR id::text = p_identifier
    LIMIT 1
);

-- --------------------------------------------------------------------------
-- 5. Function to list all active tenant schemas (for migration runner)
-- --------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION get_active_tenant_schemas()
RETURNS TABLE(schema_name VARCHAR)
LANGUAGE SQL
STABLE
PARALLEL SAFE
RETURN (
    SELECT t.schema_name
    FROM tenant_registry t
    WHERE t.status = 'active'
    ORDER BY t.slug
);
