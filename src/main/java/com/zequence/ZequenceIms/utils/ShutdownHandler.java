package com.zequence.ZequenceIms.utils;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ShutdownHandler implements DisposableBean, ApplicationListener<ContextClosedEvent> {

    private final DataSource dataSource;

    @Autowired
    public ShutdownHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void destroy() throws Exception {
        closeResources();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        closeResources();
    }

    private void closeResources() {
        System.out.println("\nRegistered Shutdown Hook: Stopping Hikari Connection Pool...");
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource ds = (HikariDataSource) dataSource;
            if (!ds.isClosed()) {
                ds.close();
            }
        }
        System.out.println("Stopped Hikari Connection Pool.\n");
    }
}