package Controller;

import Model.DAO.commerce.NotificationDAO;
import Model.entity.auth.User;
import Model.entity.commerce.Notification;
import com.google.gson.Gson;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.List;

public class NotificationServlet extends HttpServlet {

    private NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"error\":\"unauthorized\"}");
            return;
        }
        
        String userId = ((User) session.getAttribute("cus")).getUserId();

        try {
            List<Notification> notifications = notificationDAO.getUserNotification(userId);
            // Chỉ lấy 5-10 thông báo mới nhất
            int toIndex = Math.min(5, notifications.size());
            List<Notification> latest = notifications.subList(0, toIndex);

            Gson gson = new Gson();
            String json = gson.toJson(latest);
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"server error\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

}
