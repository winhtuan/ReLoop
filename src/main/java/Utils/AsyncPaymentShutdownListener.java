package Utils;

import Service.AsyncPaymentService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.logging.Logger;

/**
 * ServletContextListener để shutdown AsyncPaymentService khi ứng dụng tắt
 */
@WebListener
public class AsyncPaymentShutdownListener implements ServletContextListener {
    
    private static final Logger LOGGER = Logger.getLogger(AsyncPaymentShutdownListener.class.getName());
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("🚀 ReLoop application starting up...");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("🛑 ReLoop application shutting down, cleaning up async payment service...");
        AsyncPaymentService.shutdown();
        LOGGER.info("✅ AsyncPaymentService shutdown completed");
    }
} 