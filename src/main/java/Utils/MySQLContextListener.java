package Utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // No initialization tasks for now
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            // Unregister JDBC Driver
            DriverManager.deregisterDriver(DriverManager.getDriver("jdbc:mysql://localhost"));
            // Clean up MySQL abandoned connection thread
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.uncheckedShutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
