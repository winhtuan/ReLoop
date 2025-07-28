package Controller.AICensor;

import Utils.AppConfig;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import org.json.*;
import java.util.logging.Logger;
import java.util.logging.Level;

@MultipartConfig
public class GoogleVision extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(GoogleVision.class.getName());
    private static final int TIMEOUT_SECONDS = 30;
    private static final int MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
    
    private final String apiKey = new AppConfig().get("vision.api_key");
    private final String apiUrl = new AppConfig().get("vision.api_url");

    private static final List<String> WEAPON_KEYWORDS = Arrays.asList(
        "gun", "firearm", "weapon", "rifle", "pistol", "shotgun", "revolver", "ammunition", "bullet", "explosive", "bomb", "grenade", "knife", "dagger", "sword"
    );
    private static final List<String> DRUG_KEYWORDS = Arrays.asList(
        "drug", "drugs", "narcotic", "marijuana", "cannabis", "cocaine", "heroin", "methamphetamine", "amphetamine", "opioid", "opiate", "psychedelic", "hallucinogen", "ecstasy", "mdma", "lsd", "syringe", "needle", "bong", "pipe"
    );

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        JSONObject result = new JSONObject();

        try {
            LOGGER.info("🚀 Starting Google Vision API request");
            
            // Validate API key
            if (apiKey == null || apiKey.trim().isEmpty()) {
                LOGGER.severe("❌ Google Vision API key is null or empty");
                response.setStatus(500);
                result.put("error", "Google Vision API key not configured");
                response.getWriter().write(result.toString());
                return;
            }
            
            LOGGER.info("✅ API Key configured: " + apiKey.substring(0, 10) + "...");
            LOGGER.info("🌐 API URL: " + apiUrl);

            Part filePart = request.getPart("file");
            if (filePart == null) {
                LOGGER.warning("❌ No file uploaded");
                response.setStatus(400);
                result.put("error", "No file uploaded");
                response.getWriter().write(result.toString());
                return;
            }

            // Check file size
            if (filePart.getSize() > MAX_IMAGE_SIZE) {
                LOGGER.warning("❌ File too large: " + filePart.getSize() + " bytes");
                response.setStatus(400);
                result.put("error", "File too large. Maximum size is 10MB");
                response.getWriter().write(result.toString());
                return;
            }

            LOGGER.info("📁 Processing file: " + filePart.getSubmittedFileName() + " (" + filePart.getSize() + " bytes)");

            InputStream fileContent = filePart.getInputStream();
            byte[] imageBytes = fileContent.readAllBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            LOGGER.info("🔄 Image encoded to base64, size: " + base64Image.length() + " characters");

            JSONObject image = new JSONObject();
            image.put("content", base64Image);

            JSONArray features = new JSONArray();
            features.put(new JSONObject().put("type", "SAFE_SEARCH_DETECTION"));
            features.put(new JSONObject().put("type", "LABEL_DETECTION").put("maxResults", 15));
            features.put(new JSONObject().put("type", "WEB_DETECTION").put("maxResults", 10));

            JSONObject requestObj = new JSONObject();
            requestObj.put("image", image);
            requestObj.put("features", features);

            JSONArray requests = new JSONArray();
            requests.put(requestObj);

            JSONObject postData = new JSONObject();
            postData.put("requests", requests);

            // Append api_key to url
            String fullApiUrl = apiUrl + "?key=" + apiKey;
            LOGGER.info("📡 Sending request to Google Vision API: " + fullApiUrl);
            
            URL url = new URL(fullApiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("User-Agent", "ReLoop/1.0");
            conn.setDoOutput(true);
            conn.setConnectTimeout(TIMEOUT_SECONDS * 1000);
            conn.setReadTimeout(TIMEOUT_SECONDS * 1000);

            LOGGER.info("⏱️ Connection timeout set to " + TIMEOUT_SECONDS + " seconds");

            try (OutputStream os = conn.getOutputStream()) {
                String jsonPayload = postData.toString();
                LOGGER.info("📤 Sending JSON payload, size: " + jsonPayload.length() + " characters");
                os.write(jsonPayload.getBytes("UTF-8"));
            }

            int status = conn.getResponseCode();
            LOGGER.info("📥 Response status: " + status);
            
            InputStream is = (status < 400) ? conn.getInputStream() : conn.getErrorStream();
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }
            conn.disconnect();

            String responseBody = sb.toString();
            LOGGER.info("📄 Response body length: " + responseBody.length() + " characters");

            if (status >= 400) {
                LOGGER.severe("❌ Google Vision API error: " + status + " - " + responseBody);
                response.setStatus(status);
                result.put("error", "Vision API error: " + responseBody);
                result.put("status", status);
                response.getWriter().write(result.toString());
                return;
            }

            LOGGER.info("✅ Google Vision API request successful");
            JSONObject apiResponse = new JSONObject(responseBody);
            JSONObject detection = apiResponse.getJSONArray("responses").getJSONObject(0);

            JSONObject safeSearch = detection.optJSONObject("safeSearchAnnotation");
            String adult = safeSearch != null ? safeSearch.optString("adult", "UNKNOWN") : "UNKNOWN";
            String violence = safeSearch != null ? safeSearch.optString("violence", "UNKNOWN") : "UNKNOWN";
            String racy = safeSearch != null ? safeSearch.optString("racy", "UNKNOWN") : "UNKNOWN";

            LOGGER.info("🔍 Safe search results - Adult: " + adult + ", Violence: " + violence + ", Racy: " + racy);

            JSONArray labelAnnotations = detection.optJSONArray("labelAnnotations");
            List<String> weaponLabels = new ArrayList<>();
            if (labelAnnotations != null) {
                LOGGER.info("🏷️ Processing " + labelAnnotations.length() + " labels");
                for (int i = 0; i < labelAnnotations.length(); i++) {
                    JSONObject label = labelAnnotations.getJSONObject(i);
                    String desc = label.optString("description", "").toLowerCase();
                    for (String keyword : WEAPON_KEYWORDS) {
                        if (desc.contains(keyword)) {
                            weaponLabels.add(label.optString("description") + " (score: " + label.optDouble("score", 0) + ")");
                            LOGGER.warning("⚠️ Weapon detected: " + label.optString("description"));
                            break;
                        }
                    }
                }
            }

            JSONObject webDetection = detection.optJSONObject("webDetection");
            List<String> drugEntities = new ArrayList<>();
            if (webDetection != null && webDetection.has("webEntities")) {
                JSONArray webEntities = webDetection.getJSONArray("webEntities");
                LOGGER.info("🌐 Processing " + webEntities.length() + " web entities");
                for (int i = 0; i < webEntities.length(); i++) {
                    JSONObject entity = webEntities.getJSONObject(i);
                    String desc = entity.optString("description", "").toLowerCase();
                    for (String keyword : DRUG_KEYWORDS) {
                        if (desc.contains(keyword)) {
                            drugEntities.add(entity.optString("description") + " (score: " + entity.optDouble("score", 0) + ")");
                            LOGGER.warning("⚠️ Drug-related content detected: " + entity.optString("description"));
                            break;
                        }
                    }
                }
            }

            boolean isUnsafe = false;
            List<String> reasons = new ArrayList<>();
            if ("LIKELY".equals(adult) || "VERY_LIKELY".equals(adult)) {
                isUnsafe = true; reasons.add("Adult content detected");
            }
            if ("LIKELY".equals(violence) || "VERY_LIKELY".equals(violence)) {
                isUnsafe = true; reasons.add("Violent content detected");
            }
            if ("LIKELY".equals(racy) || "VERY_LIKELY".equals(racy)) {
                isUnsafe = true; reasons.add("Highly racy content detected");
            }
            if (!weaponLabels.isEmpty()) {
                isUnsafe = true; reasons.add("Weapon detected");
            }
            if (!drugEntities.isEmpty()) {
                isUnsafe = true; reasons.add("Possible drug-related content detected");
            }

            LOGGER.info("📊 Final moderation result - Unsafe: " + isUnsafe + ", Reasons: " + reasons);

            result.put("safeSearch", safeSearch != null ? safeSearch : JSONObject.NULL);
            result.put("weaponLabels", weaponLabels);
            result.put("drugEntities", drugEntities);
            result.put("isUnsafe", isUnsafe);
            result.put("reasons", reasons);

            response.getWriter().write(result.toString());
            LOGGER.info("✅ Google Vision moderation completed successfully");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "💥 Error in Google Vision API", ex);
            response.setStatus(500);
            result.put("error", "Internal server error: " + ex.getMessage());
            result.put("details", ex.toString());
            response.getWriter().write(result.toString());
        }
    }
}
