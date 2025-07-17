package Controller.Paid;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import org.json.JSONObject;

public class CreateWithrawQR extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/Admin/withdraw.jsp").forward(request, response);
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
        String bankCode = json.getString("bankCode");
        String accountNumber = json.getString("accountNumber");
        String accountName = json.getString("accountName");
        String amount = json.getString("amount");
        String addInfo = json.getString("addInfo");

        // Tạo QR link
        String encodedAddInfo = URLEncoder.encode(addInfo, "UTF-8");
        String encodedAccountName = URLEncoder.encode(accountName, "UTF-8");

        String qrLink = String.format(
                "https://img.vietqr.io/image/%s-%s-compact2.jpg?amount=%s&addInfo=%s&accountName=%s",
                bankCode, accountNumber, amount, encodedAddInfo, encodedAccountName
        );

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
                + "      <br><button class='close-btn' onclick='closeModal()'>Đóng</button>"
                + "    </div>"
                + "  </div>"
                + "  <script>"
                + "    function closeModal() {"
                + "      document.getElementById('qrModal').style.display = 'none';"
                + "    }"
                + "  </script>"
                + "</body>"
                + "</html>";
    }
}
