package Controller.Conversation;

import Model.DAO.conversation.ConversationDAO;
import Model.DAO.conversation.MessageDAO;
import Model.entity.conversation.Message;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
        if (user1 == null || user2 == null ) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số!");
            return;
        }

        // Tạo hoặc lấy conversation_id (String)
        String conversationId = new ConversationDAO().getOrCreateConversation(user1, user2);

        // Lấy danh sách tin nhắn
        List<Message> messages = MessageDAO.getMessagesByconversationId(conversationId);
        // Thiết lập response JSON
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Gson gson = new Gson();
        String json = gson.toJson(messages);

        out.print(json);
        out.flush();
    }
}
