package Controller.Paid;

import Model.DAO.auth.UserDao;
import Model.DAO.pay.TransactionDAO;
import Model.DAO.pay.WithdrawalRequestDAO;
import Model.entity.auth.User;
import Model.entity.pay.Transaction;
import Model.entity.pay.WithdrawalRequest;
import Utils.DBUtils;
import ch.qos.logback.core.db.dialect.DBUtil;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class CreateWithrawQR extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = DBUtils.getConnect();
        WithdrawalRequestDAO dao = new WithdrawalRequestDAO(conn);
        String withdrawalIdStr = request.getParameter("withdrawalId");
        
        // Kiểm tra null và empty trước khi parse
        if (withdrawalIdStr == null || withdrawalIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: withdrawalId parameter is required");
            return;
        }
        
        try {
            dao.rejectRequestById(Integer.parseInt(withdrawalIdStr));
            request.getRequestDispatcher("/WithdrawServlet").forward(request, response);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: Invalid withdrawalId format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Đọc JSON từ request
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        JSONObject json = new JSONObject(sb.toString());
        String withdrawalId = json.optString("withdrawalId", null);

        String bankCode = json.optString("bankCode", "");
        String accountNumber = json.optString("accountNumber", "");
        String accountName = json.optString("accountName", "");
        String amount = json.optString("amount", null);
        String addInfo = json.optString("addInfo", "");
        String userID = json.optString("userID", null);
        
        // Kiểm tra các tham số bắt buộc
        if (withdrawalId == null || withdrawalId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: withdrawalId is required");
            return;
        }
        
        if (amount == null || amount.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: amount is required");
            return;
        }
        
        if (userID == null || userID.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: userID is required");
            return;
        }

        // Tạo QR link
        String encodedAddInfo = URLEncoder.encode(addInfo, "UTF-8");
        String encodedAccountName = URLEncoder.encode(accountName, "UTF-8");

        String qrLink = String.format(
                "https://img.vietqr.io/image/%s-%s-compact2.jpg?amount=%s&addInfo=%s&accountName=%s",
                bankCode, accountNumber, amount, encodedAddInfo, encodedAccountName
        );
        //Update balance
        Connection conn = DBUtils.getConnect();
        int amountint;
        try {
            amountint = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: Invalid amount format");
            return;
        }
        int balanceBefore = new UserDao().getUserBalance(conn, userID);
        int balanceAfter = balanceBefore - amountint;
        try {
            new UserDao().updateUserBalance(conn, userID, balanceAfter);
        } catch (Exception ex) {
            Logger.getLogger(CreateWithrawQR.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Generate transaction ID đơn giản (có thể cải tiến với auto id sau)
        String transactionId = new TransactionDAO().generateTransactionId();

        Transaction log = new Transaction();
        log.setTransactionId(transactionId);
        log.setUserId(userID);
        log.setType("withdraw");
        log.setAmount(amountint);
        log.setBalanceBefore(balanceBefore);
        log.setBalanceAfter(balanceAfter);
        log.setReferenceId(null); // có thể là requestId nếu bạn muốn
        log.setDescription("Withdraw request submitted");
        log.setStatus("pending"); // vì request chưa được duyệt

        TransactionDAO logDAO = new TransactionDAO();
        logDAO.logTransaction(log);
        WithdrawalRequestDAO dao = new WithdrawalRequestDAO(conn);
        try {
            dao.approveRequestById(Integer.parseInt(withdrawalId));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: Invalid withdrawalId format");
            return;
        }

        // Trả về HTML modal
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(buildQrModalHtml(qrLink));
    }

    private String buildQrModalHtml(String qrLink) {
        return "<div class='modal' id='qrModal'>"
                + "  <div class='modal-content'>"
                + "    <h3 style='margin-bottom: 15px; font-size: 18px; font-weight: bold;'>QR Code</h3>"
                + "    <img src='" + qrLink + "' alt='QR Code' style='max-width: 300px; height: auto; display: block; margin: 0 auto;' />"
                + "    <br><button class='close-btn' style='margin-top: 10px; padding: 8px 16px; border: none; background: #f44336; color: white; border-radius: 5px; cursor: pointer; font-size: 14px;'>Close</button>"
                + "  </div>"
                + "</div>"
                + "<style>"
                + "  .modal { display: flex; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background-color: rgba(0,0,0,0.5); justify-content: center; align-items: center; z-index: 9999; }"
                + "  .modal-content { background: #fff; padding: 20px; border-radius: 10px; text-align: center; box-shadow: 0 0 10px rgba(0,0,0,0.2); max-width: 90%; }"
                + "  .close-btn:hover { background: #d32f2f !important; }"
                + "</style>";
    }
}
