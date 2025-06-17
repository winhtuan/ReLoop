package Controller.Conversation;

import Model.DAO.conversation.ConversationDAO;
import Model.DAO.conversation.MessageDAO;
import Model.DAO.post.ProductDao;
import Model.entity.conversation.Conversation;
import Model.entity.conversation.Message;
import Model.entity.post.Product;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/GetChatHistory")
public class GetChatHistory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Thiết lập charset đầu vào
        request.setCharacterEncoding("UTF-8");

        // Lấy tham số từ request
        String user1 = request.getParameter("user1");
        String user2 = request.getParameter("user2");
//        String productId = request.getParameter("productId"); // Lấy productId nếu cần

        // Kiểm tra đầu vào
        if (user1 == null || user2 == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số!");
            return;
        }

        // Tạo hoặc lấy conversation_id (String)
        Conversation conversationId = new ConversationDAO().getConversation(user1, user2);
        Product pro= new ProductDao().getProductById(conversationId.getProductId());
        // Lấy danh sách tin nhắn
        List<Message> messages = MessageDAO.getMessagesByconversationId(conversationId.getConversationId());
        // Thiết lập response JSON

        Map<String, Object> payload = new HashMap<>();
        payload.put("product", pro);
        payload.put("messages", messages);

        /*--- Trả JSON ---*/
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(new Gson().toJson(payload));
        }
    }
}
