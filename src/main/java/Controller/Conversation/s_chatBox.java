package Controller.Conversation;

import Model.DAO.post.ProductDao;
import Model.entity.post.Product;
import Utils.AppConfig;
import com.google.gson.*;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class s_chatBox extends HttpServlet {

    // <<< THAY ĐỔI 1: Lấy key từ config, không hard-code
    private static final String API_KEY = new AppConfig().get("chatbox.api_key");

    // <<< THAY ĐỔI 2: Endpoint sạch, không chứa key
    private static final String API_URL = new AppConfig().get("chatbox.api_url");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        // Kiểm tra xem API Key có tồn tại không
        if (API_KEY == null || API_KEY.isBlank()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"API Key is not configured.\"}");
            return;
        }

        // 1. Đọc message từ client
        BufferedReader reader = request.getReader();
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        JsonObject requestJson = JsonParser.parseString(requestBody.toString()).getAsJsonObject();
        String userMessage = requestJson.has("message") ? requestJson.get("message").getAsString().trim() : "";

        if (userMessage.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Message cannot be empty\"}");
            return;
        }

        // 2. Lấy context sản phẩm (Phần này giữ nguyên)
        List<Product> products = new ProductDao().getAllProducts();
        StringBuilder context = new StringBuilder();
        for (Product p : products) {
            context.append("• Tên: ").append(p.getTitle())
                   .append(" - Mô tả: ").append(p.getDescription())
                   .append(" - Giá: ").append(p.getPrice())
                   .append("\n");
        }

        String prompt =
                "Bạn là một nhân viên hỗ trợ khách hàng am hiểu và thân thiện của một sàn thương mại điện tử đồ cũ.\n"
              + "Dưới đây là danh sách một số sản phẩm chúng ta đang có:\n"
              + context
              + "\nKhách hàng hỏi: \"" + userMessage + "\"\n"
              + "\nHãy dựa vào danh sách sản phẩm trên để trả lời câu hỏi của khách hàng một cách tự nhiên bằng tiếng Việt. sử dụng dấu xuống dòng '\\n' để tách ý"
              + "Nếu câu hỏi không liên quan đến sản phẩm hoặc việc mua bán, hãy lịch sự trả lời rằng: \"Tôi chỉ có thể hỗ trợ các câu hỏi liên quan đến sản phẩm. Bạn có cần tôi giúp gì khác không ạ?\"";

        // <<< THAY ĐỔI 3: Cấu trúc JSON Body chính xác cho Gemini API
        // {
        //   "contents": [{
        //     "parts": [{
        //       "text": "Your prompt here"
        //     }]
        //   }]
        // }
        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);

        JsonArray partsArray = new JsonArray();
        partsArray.add(part);

        JsonObject content = new JsonObject();
        content.add("parts", partsArray);

        JsonArray contentsArray = new JsonArray();
        contentsArray.add(content);

        JsonObject finalBody = new JsonObject();
        finalBody.add("contents", contentsArray);

        // 4. Gửi POST request
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        // <<< THAY ĐỔI 4: Gửi API Key qua Header
        conn.setRequestProperty("x-goog-api-key", API_KEY);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = finalBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 5. Đọc phản hồi (Phần này giữ nguyên, đã rất tốt)
        int status = conn.getResponseCode();
        InputStream is = (status < 400) ? conn.getInputStream() : conn.getErrorStream();
        StringBuilder responseStr = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseStr.append(responseLine.trim());
            }
        }

        String aiReply;
        if (status == 200) {
            try {
                JsonObject jsonResponse = JsonParser.parseString(responseStr.toString()).getAsJsonObject();
                JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                    JsonObject contentResponse = firstCandidate.getAsJsonObject("content");
                    JsonArray partsResponse = contentResponse.getAsJsonArray("parts");
                    aiReply = partsResponse.get(0).getAsJsonObject().get("text").getAsString();
                } else {
                    // Trường hợp API trả về 200 OK nhưng không có nội dung (ví dụ do safety settings)
                    aiReply = "Xin lỗi, tôi không thể tạo câu trả lời cho yêu cầu này. Vui lòng thử lại với một câu hỏi khác.";
                }
            } catch (Exception e) {
                aiReply = "Lỗi xử lý phản hồi từ Gemini. Vui lòng thử lại sau.";
                e.printStackTrace(); // In lỗi ra log server để debug
            }
        } else {
            // In ra lỗi chi tiết hơn để dễ debug
            aiReply = "Lỗi khi gọi API: " + status + ". Phản hồi từ server: " + responseStr;
            System.err.println(aiReply); // In lỗi ra console của server
        }

        // 6. Gửi phản hồi về client
        JsonObject result = new JsonObject();
        result.addProperty("response", aiReply.replace("* **", "<br><br>").replace("**", "<br>-"));
        response.getWriter().write(result.toString());
    }
}   