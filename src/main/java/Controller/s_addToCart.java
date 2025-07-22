package Controller;

import Model.DAO.commerce.CartDAO;
import Model.entity.auth.Account;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class s_addToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/ReLoop/callLogin");
            return;
        }
        String productId = request.getParameter("postID");
        Account acc = (Account) request.getSession().getAttribute("user");
        String userId = acc.getUserId();
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        CartDAO cartDao = new CartDAO();
        if (!cartDao.hasCart(userId)) {
            cartDao.addNewCart(userId);
        }
        // Kiểm tra xem đã có sản phẩm này trong giỏ chưa
        int existingQuantity = cartDao.getProductQuantityInCart(userId, productId);

        if (existingQuantity > 0) {
            // Nếu đã có → tăng quantity lên 1
            if (!cartDao.isCartQuantityExceedsAvailable(userId, productId)) {
                cartDao.updateQuantity(userId, productId, existingQuantity + quantity);
            }
        } else {
            // Nếu chưa có → thêm mới với quantity = 1
            cartDao.addItemToCart(userId, productId, quantity);
        }

        int cartN = new CartDAO().getTotalQuantityByUserId(acc.getUserId());
        request.getSession().setAttribute("cartN", cartN);        
        response.setStatus(200);
    }
}
