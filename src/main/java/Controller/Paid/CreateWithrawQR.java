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
        dao.rejectRequestById(Integer.parseInt(withdrawalIdStr));
        request.getRequestDispatcher("/WithdrawServlet").forward(request, response);
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
        String withdrawalId = json.getString("withdrawalId");

        String bankCode = json.getString("bankCode");
        String accountNumber = json.getString("accountNumber");
        String accountName = json.getString("accountName");
        String amount = json.getString("amount");
        String addInfo = json.getString("addInfo");
        String userID = json.getString("userID");

        // Tạo QR link
        String encodedAddInfo = URLEncoder.encode(addInfo, "UTF-8");
        String encodedAccountName = URLEncoder.encode(accountName, "UTF-8");

        String qrLink = String.format(
                "https://img.vietqr.io/image/%s-%s-compact2.jpg?amount=%s&addInfo=%s&accountName=%s",
                bankCode, accountNumber, amount, encodedAddInfo, encodedAccountName
        );
        //Update balance
        Connection conn = DBUtils.getConnect();
        int amountint = Integer.parseInt(amount);
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
        dao.approveRequestById(Integer.parseInt(withdrawalId));

        // Trả về HTML modal
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(buildQrModalHtml(qrLink));
    }

    private String buildQrModalHtml(String qrLink) {
        return "<!DOCTYPE html>"
                + "<html lang='en'>"
                + "<head>"
                + "  <meta charset='UTF-8'>"
                + "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "  <style>"
                + "    .modal { display: flex; position: fixed; top: 0; left: 0; right: 0; bottom: 0;"
                + "      background-color: rgba(0,0,0,0.5); justify-content: center; align-items: center; }"
                + "    .modal-content { background: #fff; padding: 20px; border-radius: 10px;"
                + "      text-align: center; box-shadow: 0 0 10px rgba(0,0,0,0.2); }"
                + "    .modal-content img { max-width: 300px; height: auto; }"
                + "    .close-btn { margin-top: 10px; padding: 6px 12px; border: none; background: #f44336;"
                + "      color: white; border-radius: 5px; cursor: pointer; }"
                + "  </style>"
                + "</head>"
                + "<body>"
                + "  <div class='modal' id='qrModal'>"
                + "    <div class='modal-content'>"
                + "      <img src='" + qrLink + "' alt='QR Code' />"
                + "      <br><button class='close-btn' onclick='closeModal('qrModal')'>Đóng</button>"
                + "    </div>"
                + "  </div>"
                + "</body>"
                + "</html>";
    }
}
