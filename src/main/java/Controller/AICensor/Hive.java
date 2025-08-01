package Controller.AICensor;

import com.google.gson.JsonObject;
import Utils.AppConfig;
import com.google.gson.JsonArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Hive extends HttpServlet {

    private final String apiKey = new AppConfig().get("hive.api_key");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Test endpoint
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\": \"Hive servlet is working\"}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Đọc parameter text trực tiếp
        String textToModerate = request.getParameter("text");
        System.out.println("Parameter text: " + textToModerate);

        if (textToModerate == null || textToModerate.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing text\"}");
            return;
        }

        // Log để debug
        System.out.println("Hive API Key: " + (apiKey != null ? "Present" : "Missing"));
        System.out.println("Text to moderate: " + textToModerate);

        // Chuẩn bị JSON payload
        JsonObject input = new JsonObject();
        JsonObject inner = new JsonObject();
        inner.addProperty("text", textToModerate);

        JsonArray inputArray = new JsonArray();
        inputArray.add(inner);

        JsonObject payload = new JsonObject();
        payload.add("input", inputArray);

        System.out.println("Payload: " + payload.toString());

        try {
            // Gửi HTTP POST tới Hive
            URL url = new URL("https://api.thehive.ai/api/v3/hive/text-moderation");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);

            System.out.println("Making request to: " + url.toString());

            // Gửi body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] inputBytes = payload.toString().getBytes("utf-8");
                os.write(inputBytes, 0, inputBytes.length);
            }

            int status = conn.getResponseCode();
            System.out.println("Response status: " + status);

            BufferedReader reader = new BufferedReader(
                    new java.io.InputStreamReader(
                            status >= 400 ? conn.getErrorStream() : conn.getInputStream(), "utf-8"));

            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line.trim());
            }

            reader.close();

            String responseText = responseBuilder.toString();
            System.out.println("Response body: " + responseText);

            // Gửi response lại cho client
            response.setStatus(status);
            response.getWriter().write(responseText);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to moderate text: " + e.getMessage() + "\"}");
        }
    }
}
