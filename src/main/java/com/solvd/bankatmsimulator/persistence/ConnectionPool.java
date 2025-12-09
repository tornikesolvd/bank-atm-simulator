package com.solvd.bankatmsimulator.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import static com.solvd.bankatmsimulator.persistence.Config.*;

public final class ConnectionPool {

    private static volatile HikariDataSource ds;

    private ConnectionPool() {
        throw new IllegalStateException("Utility class, do not instantiate!");
    }

    public static DataSource getDataSource() {
        if (ds == null) {
            synchronized (ConnectionPool.class) {
                if (ds == null) {
                    HikariConfig cfg = new HikariConfig();
                    cfg.setJdbcUrl(URL);
                    cfg.setUsername(USERNAME);
                    cfg.setPassword(PASSWORD);
                    cfg.setMaximumPoolSize(POOL_SIZE);
                    cfg.setMinimumIdle(2);
                    cfg.setPoolName("bank-atm-simulator-hikari-pool");
                    cfg.addDataSourceProperty("cachePrepStmts", "true");
                    cfg.addDataSourceProperty("prepStmtCacheSize", "250");
                    cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                    cfg.setAutoCommit(false);
                    ds = new HikariDataSource(cfg);
                }
            }
        }
        return ds;
    }

    public static void close() {
        if (ds != null)
            ds.close();
    }
}
