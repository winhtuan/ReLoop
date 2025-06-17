package Controller.Conversation;

import Model.DAO.post.ProductDao;
import Model.entity.post.Product;
import Utils.AppConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.*;

public class s_chatBox extends HttpServlet {

    private static final String API_URL = new AppConfig().get("chatbox.api_url");
    
    private static final String API_KEY = new AppConfig().get("chatbox.api_key");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đọc JSON request từ client
        BufferedReader reader = request.getReader();
        StringBuilder requestData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestData.append(line);
        }

        JsonObject requestJson = JsonParser.parseString(requestData.toString()).getAsJsonObject();
        String userMessage = requestJson.has("message") ? requestJson.get("message").getAsString().trim() : "";

        if (userMessage.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Message cannot be empty\"}");
            return;
        }

        // Lấy danh sách xe từ DB
        List<Product> allProducts = new ProductDao().getAllProducts();
        StringBuilder productData = new StringBuilder();

        for (Product p : allProducts) {
            productData.
                     append(", Title: ").append(p.getTitle())
                    .append(", Description: ").append(p.getDescription())
                    .append(", Price: ").append(p.getPrice())
                    .append(", Location: ").append(p.getLocation())
                    .append(", Status: ").append(p.getStatus())
                    .append(", Priority: ").append(p.isPriority())
                    .append(", Created At: ").append(p.getCreatedAt())
                    .append(", Updated At: ").append(p.getUpdatedAt())
                    .append(System.lineSeparator());
        }
//
//        if (allProducts.isEmpty()) {
//            JsonObject emptyResp = new JsonObject();
//            emptyResp.addProperty("response", "Vui lòng hỏi câu liên quan đến danh sách xe.");
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write(emptyResp.toString());
//            return;
//        }

        // Tạo prompt kết hợp dữ liệu database và câu hỏi user
        String prompt = "You are supporter for a used goods trading floor. Please refer to the products below to answer customer questions :\n" + productData + ". Customer Question: " + userMessage + ". "
                + "Answer in Vietnamese and if question are not relate to products then answer \"Vui lòng hỏi câu liên quan đến các sản phẩm bạn cần!\"";
        // Tạo payload JSON theo chuẩn Chat Completion
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", new AppConfig().get("chatbox.model"));
        requestBody.addProperty("stream", false);

        JsonArray messages = new JsonArray();

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", prompt);

        messages.add(userMsg);
        requestBody.add("messages", messages);

        // Gửi request đến Huggingface
            URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int status = conn.getResponseCode();
            InputStream inputStream = (status < 400) ? conn.getInputStream() : conn.getErrorStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder responseStr = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseStr.append(line.trim());
            }
            br.close();

            String aiResponseText = "Vui lòng hỏi câu liên quan đến các sản phẩm bạn cần111.";
            try {
                JsonObject jsonResponse = JsonParser.parseString(responseStr.toString()).getAsJsonObject();
                if (jsonResponse.has("choices")) {
                    JsonArray choices = jsonResponse.getAsJsonArray("choices");
                    if (choices.size() > 0) {
                        JsonObject firstChoice = choices.get(0).getAsJsonObject();
                        JsonObject messageObj = firstChoice.getAsJsonObject("message");
                        if (messageObj != null && messageObj.has("content")) {
                            aiResponseText = messageObj.get("content").getAsString();
                        }
                    }
                }
        } catch (Exception e) {
                aiResponseText = "Lỗi xử lý phản hồi từ AI.";
                e.printStackTrace();
            }

        // Gửi response về client
            JsonObject clientResponse = new JsonObject();
            clientResponse.addProperty("response", aiResponseText);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(clientResponse.toString());
            }
        }
