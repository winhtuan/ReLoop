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

@MultipartConfig
public class GoogleVision extends HttpServlet {

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
            Part filePart = request.getPart("file");
            if (filePart == null) {
                response.setStatus(400);
                result.put("error", "No file uploaded");
                response.getWriter().write(result.toString());
                return;
            }

            InputStream fileContent = filePart.getInputStream();
            byte[] imageBytes = fileContent.readAllBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

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
            URL url = new URL(fullApiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.toString().getBytes("UTF-8"));
            }

            int status = conn.getResponseCode();
            InputStream is = (status < 400) ? conn.getInputStream() : conn.getErrorStream();
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }
            conn.disconnect();

            if (status >= 400) {
                response.setStatus(status);
                result.put("error", "Vision API error: " + sb.toString());
                response.getWriter().write(result.toString());
                return;
            }

            JSONObject apiResponse = new JSONObject(sb.toString());
            JSONObject detection = apiResponse.getJSONArray("responses").getJSONObject(0);

            JSONObject safeSearch = detection.optJSONObject("safeSearchAnnotation");
            String adult = safeSearch != null ? safeSearch.optString("adult", "UNKNOWN") : "UNKNOWN";
            String violence = safeSearch != null ? safeSearch.optString("violence", "UNKNOWN") : "UNKNOWN";
            String racy = safeSearch != null ? safeSearch.optString("racy", "UNKNOWN") : "UNKNOWN";

            JSONArray labelAnnotations = detection.optJSONArray("labelAnnotations");
            List<String> weaponLabels = new ArrayList<>();
            if (labelAnnotations != null) {
                for (int i = 0; i < labelAnnotations.length(); i++) {
                    JSONObject label = labelAnnotations.getJSONObject(i);
                    String desc = label.optString("description", "").toLowerCase();
                    for (String keyword : WEAPON_KEYWORDS) {
                        if (desc.contains(keyword)) {
                            weaponLabels.add(label.optString("description") + " (score: " + label.optDouble("score", 0) + ")");
                            break;
                        }
                    }
                }
            }

            JSONObject webDetection = detection.optJSONObject("webDetection");
            List<String> drugEntities = new ArrayList<>();
            if (webDetection != null && webDetection.has("webEntities")) {
                JSONArray webEntities = webDetection.getJSONArray("webEntities");
                for (int i = 0; i < webEntities.length(); i++) {
                    JSONObject entity = webEntities.getJSONObject(i);
                    String desc = entity.optString("description", "").toLowerCase();
                    for (String keyword : DRUG_KEYWORDS) {
                        if (desc.contains(keyword)) {
                            drugEntities.add(entity.optString("description") + " (score: " + entity.optDouble("score", 0) + ")");
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

            result.put("safeSearch", safeSearch != null ? safeSearch : JSONObject.NULL);
            result.put("weaponLabels", weaponLabels);
            result.put("drugEntities", drugEntities);
            result.put("isUnsafe", isUnsafe);
            result.put("reasons", reasons);

            response.getWriter().write(result.toString());

        } catch (Exception ex) {
            response.setStatus(500);
            result.put("error", ex.getMessage());
            response.getWriter().write(result.toString());
        }
    }
}
