package com.bpmplatform.infrastructure.multitenant;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class TenantSchemaMigrator {

    private final DataSource dataSource;
    private final String tenantMigrationsLocation;

    public TenantSchemaMigrator(DataSource dataSource,
                                @Value("${app.flyway.tenant-migrations-location:classpath:db/migration/tenant}") String location) {
        this.dataSource = dataSource;
        this.tenantMigrationsLocation = location;
    }

    public void provisionTenantSchema(String tenantId) {
        Flyway flyway = createTenantFlyway(tenantId);
        flyway.migrate();
    }

    public void migrateAllTenants(Iterable<String> tenantIds) {
        for (String tenantId : tenantIds) {
            provisionTenantSchema(tenantId);
        }
    }

    private Flyway createTenantFlyway(String tenantId) {
        FluentConfiguration config = Flyway.configure()
                .dataSource(dataSource)
                .schemas(tenantId)
                .locations(tenantMigrationsLocation)
                .createSchemas(true)
                .table("flyway_schema_history")
                .outOfOrder(false);

        return config.load();
    }
}
