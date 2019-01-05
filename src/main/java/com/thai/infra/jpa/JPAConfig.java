package com.thai.infra.jpa;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.metadata.HikariDataSourcePoolMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JPAConfig {
    @Value("${db.main.host}")
    String dbHost;

    @Value("${db.main.username}")
    String dbUsername;

    @Value("${db.main.password}")
    String dbPassword;

    @Value("${db.main.name}")
    String dbName;

    @Value("${db.main.pool:10}")
    int dbPoolMaxSize;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(
                dbHost + "/" + dbName + "?" +
                        "useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=UTC");

        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setMaximumPoolSize(dbPoolMaxSize);

        Properties dataSourceProps = new Properties();
        dataSourceProps.setProperty("cachePrepStmts", "true");
        dataSourceProps.setProperty("prepStmtCacheSize", "250");
        dataSourceProps.setProperty("prepStmtCacheSqlLimit", "2048");

        dataSource.setDataSourceProperties(dataSourceProps);

        return dataSource;
    }

    @Bean
    public HikariDataSourcePoolMetadata dataSourceMetaData() {
        return new HikariDataSourcePoolMetadata((HikariDataSource) dataSource());
    }

    @Bean
    public JdbcTemplate jdbc() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Autowired EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
