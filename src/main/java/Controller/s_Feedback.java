package Controller;

import Model.DAO.commerce.FeedbackDao;
import Model.entity.commerce.Feedback;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet to handle fetching feedback for a product.
 */
public class s_Feedback extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productId = request.getParameter("productId");
        if (productId == null || productId.isEmpty()) {
            productId = (String) request.getAttribute("productId");
        }

        if (productId != null) {
            FeedbackDao feedbackDao = new FeedbackDao();
            try {
                List<Feedback> feedbackList = feedbackDao.getFeedbackByProductId(productId);
                request.setAttribute("feedbackList", feedbackList); // Đặt vào request
            } catch (SQLException e) {
                request.setAttribute("errorMessage", "Failed to load feedback: " + e.getMessage());
            }
        }
        // Không forward, để s_productDetail xử lý
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Gọi lại doGet để xử lý
    }
}