package com.bpmplatform.infrastructure.multitenant;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class TenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<String> {

    private static final Logger log = LoggerFactory.getLogger(TenantConnectionProviderImpl.class);
    private static final String DEFAULT_TENANT = "public";
    private final DataSource dataSource;

    public TenantConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return dataSource;
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return dataSource;
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = selectDataSource(tenantIdentifier).getConnection();
        String schema = tenantIdentifier != null ? tenantIdentifier : DEFAULT_TENANT;
        connection.setSchema(schema);
        log.debug("Setting connection schema to: {}", schema);
        return connection;
    }
}
