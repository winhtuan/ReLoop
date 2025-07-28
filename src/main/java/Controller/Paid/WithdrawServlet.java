/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Paid;

import Model.DAO.auth.UserDao;
import Model.DAO.pay.TransactionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Model.DAO.pay.WithdrawalRequestDAO;
import Model.entity.auth.User;
import Model.entity.pay.Transaction;
import Model.entity.pay.WithdrawalRequest;
import Utils.DBUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Thanh Loc
 */
public class WithdrawServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        WithdrawalRequestDAO wdao = new WithdrawalRequestDAO(DBUtils.getConnect());
        List<WithdrawalRequest> list = wdao.getAllRequests();
        
        request.setAttribute("withdrawList", list);
        request.getRequestDispatcher("/JSP/Admin/withdraw.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy session user (giả sử bạn đã lưu account trong session)
        HttpSession session = request.getSession(false);

        String userId = ((Model.entity.auth.Account) session.getAttribute("user")).getUserId();

        // Lấy form data
        String bankCode = request.getParameter("bankCode");
        String accountNumber = request.getParameter("accountNumber");
        String accountName = request.getParameter("accountName");
        String amountStr = request.getParameter("amount");
        String addInfo = request.getParameter("addInfo");

        try {
            int amount = Integer.parseInt(amountStr);

            WithdrawalRequest requestModel = new WithdrawalRequest();
            requestModel.setUserId(userId);
            requestModel.setBankCode(bankCode);
            requestModel.setAccountNumber(accountNumber);
            requestModel.setAccountName(accountName);
            requestModel.setAmount(amount);
            requestModel.setAddInfo(addInfo);

            Connection conn = DBUtils.getConnect();
            WithdrawalRequestDAO dao = new WithdrawalRequestDAO(conn);

            boolean success = dao.insertWithdrawalRequest(requestModel);
            if (success) {
                
                request.setAttribute("notifyMessage", "Withdrawal request submitted successfully!");
            } else {
                request.setAttribute("notifyMessage", "Failed to submit withdrawal request.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("notifyMessage", "Error processing withdrawal request.");
        }

        // Forward về lại trang hoặc modal
        request.getRequestDispatcher("/s_userProfile").forward(request, response);
    }

}
