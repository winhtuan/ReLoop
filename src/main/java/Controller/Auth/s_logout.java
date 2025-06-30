package Controller.Auth;

import Model.DAO.auth.AccountDao;
import Model.entity.auth.User;
import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class s_logout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        User u=(User)request.getSession().getAttribute("cus");
        new AccountDao().updateOfflineAt(u.getUserId());
        // Xóa session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // Chuyển hướng về trang login
        response.sendRedirect("home");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }

}
