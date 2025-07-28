package Controller.Admin;

import Model.DAO.admin.AdminPostDAO;
import Model.entity.auth.User;
import Model.entity.post.Product;
import Model.entity.post.ProductImage;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ApprovalPost", urlPatterns = {"/ApprovalPost"})
public class ApprovalPost extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AdminPostDAO ad = new AdminPostDAO();
        List<Product> productList = ad.approvalPost();

// Duyệt từng product để lấy ảnh
        Map<String, String> imageMap = new HashMap<>();
        for (Product p : productList) {
            List<ProductImage> images = ad.image(p.getProductId());
            if (images != null && !images.isEmpty()) {
                imageMap.put(p.getProductId(), images.get(0).getImageUrl()); // lấy 1 ảnh đầu
            } else {
                imageMap.put(p.getProductId(), "https://via.placeholder.com/60");
            }
        }

        request.setAttribute("approvalPosts", productList);
        request.setAttribute("imageMap", imageMap);
        HttpSession session = request.getSession(false);
            if(session != null){
                User user = (User) session.getAttribute("cus");
                if(user.getFullName() != null && user.getPhoneNumber() != null && user.getAddress() != null){
                    request.getRequestDispatcher("/JSP/Admin/postModeration.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher("s_userProfile").forward(request, response);
                }
            }else{
                request.getRequestDispatcher("/JSP/Admin/JoinIn.jsp").forward(request, response);
            }   
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
