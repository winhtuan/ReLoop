package Controller.Paid;

import Model.DAO.commerce.OrderDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PayOSCallbackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Đọc dữ liệu JSON từ body
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        String status = json.get("status").getAsString();
        Long orderCode = json.get("orderCode").getAsLong();

        // TODO: Validate callback (nếu PayOS cung cấp checksum)
        // Cập nhật trạng thái trong DB
        new OrderDao().updateOrderStatusByOrderCode(orderCode, status);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
