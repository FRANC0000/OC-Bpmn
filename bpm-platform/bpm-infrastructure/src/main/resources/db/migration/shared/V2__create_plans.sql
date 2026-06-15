CREATE TABLE IF NOT EXISTS plans (
    id              UUID PRIMARY KEY,
    code            VARCHAR(50) NOT NULL UNIQUE,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    price           DECIMAL(10,2) NOT NULL DEFAULT 0,
    currency        VARCHAR(3) NOT NULL DEFAULT 'USD',
    max_users       INTEGER,
    max_processes   INTEGER,
    max_instances   INTEGER,
    max_documents   INTEGER,
    max_storage_mb  BIGINT,
    sla_percent     DECIMAL(5,2) NOT NULL DEFAULT 99.5,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

INSERT INTO plans (id, code, name, description, price, max_users, max_processes, max_instances, max_documents, sla_percent)
VALUES
    (gen_random_uuid(), 'basic', 'Basic', 'Para equipos pequeños', 0, 10, 5, 100, 50, 99.5),
    (gen_random_uuid(), 'professional', 'Professional', 'Para empresas en crecimiento', 299.00, 50, 50, 5000, 500, 99.9),
    (gen_random_uuid(), 'enterprise', 'Enterprise', 'Para grandes organizaciones', 999.00, NULL, NULL, NULL, NULL, 99.99)
ON CONFLICT (code) DO NOTHING;
