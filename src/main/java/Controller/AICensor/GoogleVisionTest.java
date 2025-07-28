package Controller.AICensor;

import Utils.AppConfig;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.*;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet("/googlevision-test")
public class GoogleVisionTest extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(GoogleVisionTest.class.getName());
    private static final int TIMEOUT_SECONDS = 30;
    
    private final String apiKey = new AppConfig().get("vision.api_key");
    private final String apiUrl = new AppConfig().get("vision.api_url");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        JSONObject result = new JSONObject();

        try {
            LOGGER.info("🧪 Starting Google Vision API connection test");
            
            // Test 1: Check configuration
            result.put("test1_config", new JSONObject()
                .put("apiKey", apiKey != null ? apiKey.substring(0, 10) + "..." : "NULL")
                .put("apiUrl", apiUrl)
                .put("status", apiKey != null && apiUrl != null ? "PASS" : "FAIL")
            );

            if (apiKey == null || apiUrl == null) {
                LOGGER.severe("❌ Configuration test failed");
                result.put("error", "API key or URL not configured");
                response.getWriter().write(result.toString());
                return;
            }

            // Test 2: Test network connectivity
            try {
                URL testUrl = new URL("https://www.google.com");
                HttpURLConnection testConn = (HttpURLConnection) testUrl.openConnection();
                testConn.setConnectTimeout(5000);
                testConn.setReadTimeout(5000);
                int testStatus = testConn.getResponseCode();
                testConn.disconnect();
                
                result.put("test2_network", new JSONObject()
                    .put("googleConnectivity", testStatus == 200 ? "PASS" : "FAIL")
                    .put("status", testStatus)
                );
                
                LOGGER.info("🌐 Network connectivity test: " + (testStatus == 200 ? "PASS" : "FAIL"));
            } catch (Exception e) {
                result.put("test2_network", new JSONObject()
                    .put("googleConnectivity", "FAIL")
                    .put("error", e.getMessage())
                );
                LOGGER.warning("❌ Network connectivity test failed: " + e.getMessage());
            }

            // Test 3: Test Google Vision API with a simple image
            try {
                // Create a simple 1x1 pixel PNG image (base64 encoded)
                String testImageBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==";
                
                JSONObject image = new JSONObject();
                image.put("content", testImageBase64);

                JSONArray features = new JSONArray();
                features.put(new JSONObject().put("type", "LABEL_DETECTION").put("maxResults", 1));

                JSONObject requestObj = new JSONObject();
                requestObj.put("image", image);
                requestObj.put("features", features);

                JSONArray requests = new JSONArray();
                requests.put(requestObj);

                JSONObject postData = new JSONObject();
                postData.put("requests", requests);

                String fullApiUrl = apiUrl + "?key=" + apiKey;
                LOGGER.info("📡 Testing Google Vision API: " + fullApiUrl);
                
                URL url = new URL(fullApiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("User-Agent", "ReLoop/1.0");
                conn.setDoOutput(true);
                conn.setConnectTimeout(TIMEOUT_SECONDS * 1000);
                conn.setReadTimeout(TIMEOUT_SECONDS * 1000);

                try (OutputStream os = conn.getOutputStream()) {
                    String jsonPayload = postData.toString();
                    os.write(jsonPayload.getBytes("UTF-8"));
                }

                int status = conn.getResponseCode();
                InputStream is = (status < 400) ? conn.getInputStream() : conn.getErrorStream();
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);
                }
                conn.disconnect();

                String responseBody = sb.toString();
                
                if (status >= 400) {
                    result.put("test3_vision_api", new JSONObject()
                        .put("status", "FAIL")
                        .put("httpStatus", status)
                        .put("error", responseBody)
                    );
                    LOGGER.severe("❌ Google Vision API test failed: " + status + " - " + responseBody);
                } else {
                    JSONObject apiResponse = new JSONObject(responseBody);
                    result.put("test3_vision_api", new JSONObject()
                        .put("status", "PASS")
                        .put("httpStatus", status)
                        .put("response", apiResponse)
                    );
                    LOGGER.info("✅ Google Vision API test successful");
                }
                
            } catch (Exception e) {
                result.put("test3_vision_api", new JSONObject()
                    .put("status", "FAIL")
                    .put("error", e.getMessage())
                    .put("exception", e.toString())
                );
                LOGGER.log(Level.SEVERE, "❌ Google Vision API test failed", e);
            }

            // Summary
            boolean allTestsPassed = true;
            if (result.has("test1_config")) {
                allTestsPassed &= "PASS".equals(result.getJSONObject("test1_config").getString("status"));
            }
            if (result.has("test2_network")) {
                allTestsPassed &= "PASS".equals(result.getJSONObject("test2_network").getString("googleConnectivity"));
            }
            if (result.has("test3_vision_api")) {
                allTestsPassed &= "PASS".equals(result.getJSONObject("test3_vision_api").getString("status"));
            }

            result.put("summary", new JSONObject()
                .put("allTestsPassed", allTestsPassed)
                .put("timestamp", System.currentTimeMillis())
            );

            LOGGER.info("📊 Test summary - All tests passed: " + allTestsPassed);
            response.getWriter().write(result.toString());

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "💥 Error in Google Vision test", ex);
            response.setStatus(500);
            result.put("error", "Test failed: " + ex.getMessage());
            result.put("details", ex.toString());
            response.getWriter().write(result.toString());
        }
    }
} 