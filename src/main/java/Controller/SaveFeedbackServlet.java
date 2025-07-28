package Controller;

import Model.DAO.commerce.FeedbackDao;
import Model.entity.auth.Account;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Servlet to handle saving feedback (rating and comment) for an order.
 */
public class SaveFeedbackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account account = (Account) req.getSession().getAttribute("user");
        String orderId = req.getParameter("orderId");
        String ratingStr = req.getParameter("rating");
        String comment = req.getParameter("comment");
        String checkOnly = req.getParameter("checkOnly"); // Thêm tham số checkOnly

        if (account != null && orderId != null) {
            try {
                FeedbackDao feedbackDao = new FeedbackDao();
                boolean hasFeedback = feedbackDao.hasFeedback(orderId, account.getUserId());

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();

                if ("true".equalsIgnoreCase(checkOnly)) {
                    // Chỉ kiểm tra
                    out.print("{\"success\": " + !hasFeedback + ", \"message\": \"" 
                            + (hasFeedback ? "You have already provided feedback for this order." : "Ready to provide feedback.") + "\"}");
                } else if (!hasFeedback && ratingStr != null) {
                    // Lưu feedback
                    int rating = Integer.parseInt(ratingStr);
                    if (rating < 1 || rating > 5) {
                        throw new IllegalArgumentException("Rating must be between 1 and 5.");
                    }
                    boolean success = feedbackDao.saveFeedback(orderId, account.getUserId(), rating, comment);
                    out.print("{\"success\": " + success + ", \"message\": \"" 
                            + (success ? "Feedback saved successfully." : "Failed to save feedback.") + "\"}");
                } else {
                    out.print("{\"success\": false, \"message\": \"You have already provided feedback for this order.\"}");
                }
                out.flush();

            } catch (NumberFormatException e) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print("{\"success\": false, \"message\": \"Invalid rating value.\"}");
                out.flush();
            } catch (SQLException e) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print("{\"success\": false, \"message\": \"Database error.\"}");
                out.flush();
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
                out.flush();
            }
        } else {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print("{\"success\": false, \"message\": \"User or order ID is missing.\"}");
            out.flush();
        }
    }
}