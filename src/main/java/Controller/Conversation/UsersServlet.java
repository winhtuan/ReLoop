package Controller.Conversation;

import Model.DAO.auth.UserDao;
import Model.DAO.conversation.ConversationDAO;
import Model.entity.auth.Account;
import Model.entity.auth.User;
import Model.entity.conversation.Conversation;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Lấy user hiện tại từ session
        Account currentUser = (Account) req.getSession().getAttribute("user");
        if (currentUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Lấy thông tin Customer hiện tại
        User user = new UserDao().getUserById(currentUser.getUserId());

        // Lấy danh sách tất cả Customer đã có cuộc trò chuyện với user
        List<User> users = new UserDao().listAllCustomers(currentUser.getUserId());

        req.setAttribute("cus", user);
        req.setAttribute("userList", users);
        req.getRequestDispatcher("JSP/Conversation/chat.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sellerId = req.getParameter("sellerId");
        String productId = req.getParameter("productId");
        Account currentUser = (Account) req.getSession().getAttribute("user");
        if (currentUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // Lấy Customer hiện tại
        User user = new UserDao().getUserById(currentUser.getUserId());
        Conversation conver= new ConversationDAO().getConversation(currentUser.getUserId(), sellerId);
        if(conver == null)
        {
            try {
                conver = new ConversationDAO().createConversation(currentUser.getUserId(), sellerId, productId);
            } catch (SQLException ex) {
                Logger.getLogger(UsersServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(!productId.equalsIgnoreCase(conver.getProductId()))
        {
            new ConversationDAO().updateProductId(conver.getConversationId(), productId);
        }
        // Lấy danh sách user đã trò chuyện
        List<User> users = new UserDao().listAllCustomers(currentUser.getUserId());

        // Đưa người đang chat lên đầu danh sách
        User c = users.stream().filter(t -> t.getUserId().equals(sellerId)).findFirst().orElse(null);
        if (c != null) {
            users.remove(c);
            users.add(0, c);
        }
        for(User a:users)
        {
            System.out.println(a);
        }
        req.setAttribute("sellid", sellerId);
        req.setAttribute("cus", user);
        req.setAttribute("userList", users);
        req.getRequestDispatcher("JSP/Conversation/chat.jsp").forward(req, resp);
    }
}
