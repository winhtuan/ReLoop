/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Conversation;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Thanh Loc
 */
@MultipartConfig
public class UploadImageServlet extends HttpServlet {

    private String IMAGE_DIR;

    @Override
    public void init() throws ServletException {
        String realPath = getServletContext().getRealPath("/img/message-img");
        if (realPath == null) {
            throw new ServletException("Cannot resolve real path for img/message-img");
        }

        File imageDir = new File(realPath);
        if (!imageDir.exists()) {
            boolean created = imageDir.mkdirs();
            if (!created) {
                throw new ServletException("Failed to create img/message-img directory");
            }
        }

        IMAGE_DIR = imageDir.getAbsolutePath();
    }

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
            out.println("<title>Servlet UploadImageServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UploadImageServlet at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fromUserId = req.getParameter("fromUserId");
        String toUserId = req.getParameter("toUserId");

        List<String> imageUrls = new ArrayList<>();

        for (Part part : req.getParts()) {
            if (part.getName().equals("images") && part.getSize() > 0) {
                String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                String uniqueName = UUID.randomUUID() + "_" + filename;
                File saveFile = new File(IMAGE_DIR, uniqueName);

                try (InputStream is = part.getInputStream(); FileOutputStream fos = new FileOutputStream(saveFile)) {
                    is.transferTo(fos);
                }

                String dbImagePath = "/images/" + uniqueName;
                imageUrls.add(dbImagePath);

            }
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Trả về mảng JSON: {"imageUrls": ["/images/xxx", "/images/yyy"]}
        String json = new Gson().toJson(Collections.singletonMap("imageUrls", imageUrls));
        resp.getWriter().write(json);
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
