package Controller;

import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Model.Account;
import Repository.AccountRep;
import Repository.CustomerRep;

public class s_regisGoogle extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getAttribute("erormess") != null) {
            request.getRequestDispatcher("registerGoogle.jsp").forward(request, response);
            return;
        }

        String address = request.getParameter("address");
        String phone = request.getParameter("Phone");
        Account user = (Account) request.getSession().getAttribute("user");

        // Tạo customer và lấy userId (trước là customerId)
        int userId = CustomerRep.addCustomer(address, phone, user.getEmail());

        // Thêm vào bảng Account (userId mới)
        AccountRep.addAccount(
                user.getEmail(),
                "user",
                userId
        );

        user.setRole("user");
        user.setUserId(userId);  // sửa setCustomerId thành setUserId
        user = AccountRep.getAccountByEmail(user.getEmail());

        String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
        if (redirectUrl != null) {
            request.getSession().removeAttribute("redirectUrl");
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("s_Car");
        }
    }
}
