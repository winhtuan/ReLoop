package Service;

import Model.DAO.pay.PaymentDao;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsyncPaymentService {
    
    private static final Logger LOGGER = Logger.getLogger(AsyncPaymentService.class.getName());
    private static final int PAYMENT_TIMEOUT_SECONDS = 10;
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final int RETRY_DELAY_MS = 1000;
    
    // Thread pool để xử lý async operations
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    
    public static CompletableFuture<Boolean> createPaymentAsync(String orderId, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            int attempts = 0;
            
            while (attempts < MAX_RETRY_ATTEMPTS) {
                try {
                    attempts++;
                    LOGGER.info("Creating payment for order " + orderId + " (attempt " + attempts + "/" + MAX_RETRY_ATTEMPTS + ")");
                    
                    String paymentId = new PaymentDao().generatePaymentId();
                    boolean success = new PaymentDao().createPayment(paymentId, orderId, amount, "pending");
                    
                    if (success) {
                        LOGGER.info("✅ Payment created successfully for order " + orderId + " with payment ID: " + paymentId);
                        return true;
                    } else {
                        LOGGER.warning("❌ Failed to create payment for order " + orderId + " (attempt " + attempts + ")");
                        if (attempts < MAX_RETRY_ATTEMPTS) {
                            LOGGER.info("⏳ Retrying in " + RETRY_DELAY_MS + "ms...");
                            Thread.sleep(RETRY_DELAY_MS);
                        }
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "💥 SQL error creating payment for order " + orderId + " (attempt " + attempts + ")", e);
                    if (attempts < MAX_RETRY_ATTEMPTS) {
                        try {
                            Thread.sleep(RETRY_DELAY_MS);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            LOGGER.warning("🛑 Payment creation interrupted for order " + orderId);
                            return false;
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.warning("🛑 Payment creation interrupted for order " + orderId);
                    return false;
                }
            }
            
            LOGGER.severe("💥 Failed to create payment for order " + orderId + " after " + MAX_RETRY_ATTEMPTS + " attempts");
            return false;
        }, executorService);
    }
    
    public static Boolean waitForPaymentCreation(String orderId, int amount) 
            throws TimeoutException, InterruptedException, ExecutionException {
        
        CompletableFuture<Boolean> paymentFuture = createPaymentAsync(orderId, amount);
        return paymentFuture.get(PAYMENT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }
    
    public static void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            LOGGER.info("🔄 Shutting down AsyncPaymentService executor...");
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static Boolean checkPaymentExists(String orderId) {
        try {
            String paymentId = new PaymentDao().getPaymentIdByOrderId(orderId);
            return paymentId != null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking payment existence for order " + orderId, e);
            return false;
        }
    }
} 