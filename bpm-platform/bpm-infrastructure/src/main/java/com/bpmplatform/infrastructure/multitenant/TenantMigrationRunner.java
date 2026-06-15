package com.bpmplatform.infrastructure.multitenant;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
@Profile("!test")
public class TenantMigrationRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TenantMigrationRunner.class);

    private final DataSource dataSource;
    private final String sharedMigrationsLocation;
    private final String tenantMigrationsLocation;

    public TenantMigrationRunner(DataSource dataSource,
                                 @Value("${app.flyway.shared-migrations-location:classpath:db/migration/shared}") String shared,
                                 @Value("${app.flyway.tenant-migrations-location:classpath:db/migration/tenant}") String tenant) {
        this.dataSource = dataSource;
        this.sharedMigrationsLocation = shared;
        this.tenantMigrationsLocation = tenant;
    }

    @Override
    public void run(String... args) {
        log.info("Running shared schema migrations...");
        migrateSharedSchema();

        log.info("Running tenant schema migrations for existing tenants...");
        migrateExistingTenants();
    }

    private void migrateSharedSchema() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas("public")
                .locations(sharedMigrationsLocation)
                .createSchemas(true)
                .table("flyway_schema_history")
                .load();

        flyway.migrate();
    }

    private void migrateExistingTenants() {
        List<String> tenantSchemas = findAllTenantSchemas();
        for (String schema : tenantSchemas) {
            log.info("Migrating tenant schema: {}", schema);
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .schemas(schema)
                    .locations(tenantMigrationsLocation)
                    .createSchemas(true)
                    .table("flyway_schema_history")
                    .outOfOrder(true)
                    .load();
            flyway.migrate();
        }
    }

    private List<String> findAllTenantSchemas() {
        var sql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name LIKE 'tenant_%'";
        try (var stmt = dataSource.getConnection().createStatement();
             var rs = stmt.executeQuery(sql)) {
            var schemas = new java.util.ArrayList<String>();
            while (rs.next()) {
                schemas.add(rs.getString(1));
            }
            return schemas;
        } catch (Exception e) {
            log.warn("Could not find existing tenant schemas: {}", e.getMessage());
            return List.of();
        }
    }
}
