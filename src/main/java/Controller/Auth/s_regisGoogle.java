package Controller.Auth;

import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Model.Entity.Account;
import Model.DAO.AccountDao;
import Model.DAO.CustomerDao;

public class s_regisGoogle extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getAttribute("erormess") != null) {
            request.getRequestDispatcher("JSP/Authenticate/registerGoogle.jsp").forward(request, response);
            return;
        }

        String address = request.getParameter("address");
        String phone = request.getParameter("Phone");
        Account user = (Account) request.getSession().getAttribute("user");

        // Tạo customer và lấy userId (trước là customerId)
        int userId = CustomerDao.addCustomer(address, phone, user.getEmail());

        // Thêm vào bảng Account (userId mới)
        AccountDao.addAccount(
                user.getEmail(),
                "user",
                userId
        );

        user.setRole("user");
        user.setUserId(userId);  // sửa setCustomerId thành setUserId
        user = AccountDao.getAccountByEmail(user.getEmail());

        String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
        if (redirectUrl != null) {
            request.getSession().removeAttribute("redirectUrl");
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("index.html");
            
        }
    }
}
