/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.DAO.auth.UserDao;
import Model.DAO.post.ReportDAO;
import Model.entity.auth.User;
import Model.entity.post.ProductReport;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author Admin
 */
@WebServlet(name = "DenounceServlet", urlPatterns = {"/DenounceServlet"})
public class DenounceServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DenounceServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DenounceServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("cus") != null) {
                User user = (User) session.getAttribute("cus");
                String productId = request.getParameter("productId");
                String us = user.getUserId();
                String reason = request.getParameter("reason");
                String description = request.getParameter("description");
                PrintWriter out = response.getWriter();
                ReportDAO dao = new ReportDAO();
                String reportId = dao.generateNextReportId();
                ProductReport report = new ProductReport();
                report.setReportId(reportId);
                report.setProductId(productId);
                report.setUserId(us);
                report.setReason(reason);
                report.setDescription(description);
                report.setReportedAt(new Date());
                report.setStatus("pending");
                out.print(report.toString());

                boolean success = dao.insertReport(report);

                if (success) {
                    request.setAttribute("reportSuccess", "Report submitted!");
                } else {
                    request.setAttribute("reportFail", "Submission failed.");
                    out.print("loiservlet");
                }

                // Điều hướng tới trang sau khi xử lý
//            request.getRequestDispatcher("somePage.jsp").forward(request, response);
                request.setAttribute("cus", new UserDao().getUserById(request.getParameter("userid")));
                request.getRequestDispatcher("JSP/Home/HomePage.jsp").forward(request, response);
            } else {
                response.sendRedirect("login.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace(); // in ra lỗi trong console
            response.setContentType("text/plain");
            response.getWriter().println("Có lỗi xảy ra: " + e.getMessage());
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
