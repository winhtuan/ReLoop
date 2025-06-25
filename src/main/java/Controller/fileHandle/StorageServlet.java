package Controller.fileHandle; // Đảm bảo đúng package của bạn

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/storage") // Ánh xạ tới URL /api/storage
public class StorageServlet extends HttpServlet {

    private static final Gson GSON = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Drive driveService = GoogleDriveConfig.getDriveService(); // Lấy Drive service từ config
            About about = driveService.about().get().setFields("storageQuota").execute();

            long totalStorageBytes = about.getStorageQuota().getLimit();
            long usedStorageBytes = about.getStorageQuota().getUsage();

            Map<String, Long> storageInfo = new HashMap<>();
            storageInfo.put("total", totalStorageBytes);
            storageInfo.put("used", usedStorageBytes);

            response.getWriter().write(GSON.toJson(storageInfo));

        } catch (Exception e) {
            System.err.println("Failed to fetch storage info: " + e.getMessage());
            e.printStackTrace(); // In stack trace để debug chi tiết hơn
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(GSON.toJson(Map.of("error", "Failed to fetch storage info", "details", e.getMessage())));
        }
    }
}
