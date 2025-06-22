package Controller.fileHandle;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig; // <-- THÊM DÒNG NÀY
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part; // <-- THÊM DÒNG NÀY

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/api/files/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
) // Cấu hình cho multipart upload
public class FilesServlet extends HttpServlet {

    private static final Gson gson = new Gson();

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Drive driveService = GoogleDriveConfig.getDriveService();
        
        if (pathInfo != null && pathInfo.startsWith("/download/")) {
            // Xử lý download file (binary data)
            String fileId = pathInfo.substring("/download/".length());
            try {
                File fileMeta = driveService.files().get(fileId)
                        .setFields("name")
                        .execute();

                response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileMeta.getName(), "UTF-8") + "\"");
                // Lấy OutputStream CHỈ KHI CẦN
                OutputStream outputStream = response.getOutputStream();
                
                try (InputStream inputStream = driveService.files().get(fileId).executeMediaAsInputStream()) {
                    inputStream.transferTo(outputStream);
                }
                
                outputStream.flush();
            } catch (IOException e) {
                System.err.println("Download failed for file ID " + fileId + ": " + e.getMessage());
                // Nếu có lỗi, chúng ta vẫn cần trả về JSON, nên phải lấy Writer ở đây
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(new ErrorResponse("File not found or access denied.")));
                out.flush(); // Đảm bảo flush
            }
        } else {
            // Xử lý các yêu cầu khác (JSON responses)
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            // Lấy PrintWriter CHỈ KHI CẦN
            PrintWriter out = response.getWriter();

            if (pathInfo == null || pathInfo.equals("/")) {
                // API: List files (GET /api/files)
                String parentId = request.getParameter("folderId");
                if (parentId == null || parentId.isEmpty() || parentId.equals("root")) {
                    parentId = "1kau-OjthtS6xcJf0IoX6XaY9SUs35E5Y";
                }

                String query = String.format("'%s' in parents and trashed=false", parentId);
                FileList result = driveService.files().list()
                        .setQ(query)
                        .setFields("files(id, name, mimeType, size, modifiedTime, webViewLink, iconLink)")
                        .setSpaces("drive")
                        .execute();
                List<File> files = result.getFiles();
                out.print(gson.toJson(files));
            } else if (pathInfo.startsWith("/share/")) {
                // API: Get share link (GET /api/files/share/{id})
                String fileId = pathInfo.substring("/share/".length());
                try {
                    String shareLink = GoogleDriveUtil.getOrCreateShareLink(fileId);
                    out.print(gson.toJson(new ShareLinkResponse(shareLink)));
                } catch (IOException e) {
                    System.err.println("Share link retrieval failed for file ID " + fileId + ": " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print(gson.toJson(new ErrorResponse("Failed to get share link: " + e.getMessage())));
                }
            } else {
                // Xử lý các pathInfo không hợp lệ hoặc không xác định
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(new ErrorResponse("API endpoint not found for: " + pathInfo)));
            }
            out.flush(); // Đảm bảo flush
        }
    }

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    resp.setContentType("application/json; charset=UTF-8");
    PrintWriter out = resp.getWriter();

    String parentId = "1kau-OjthtS6xcJf0IoX6XaY9SUs35E5Y";
    List<FileUploadResponse> uploaded = new ArrayList<>();

    for (Part part : req.getParts()) {
        switch (part.getName()) {
            case "folderId" : {
                try (InputStream is = part.getInputStream()) {
                    parentId = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                }
            }
            case "file" : {
                if (part.getSubmittedFileName() == null || part.getSubmittedFileName().isBlank()) continue;

                try (InputStream in = part.getInputStream()) {
                    File meta = new File()
                            .setName(part.getSubmittedFileName())
                            .setParents(List.of(parentId));

                    File gFile = GoogleDriveConfig.getDriveService()
                            .files()
                            .create(meta, new InputStreamContent(part.getContentType(), in))
                            .setFields("id, name, webViewLink")
                            .execute();

                    String share = GoogleDriveUtil.getOrCreateShareLink(gFile.getId());
                    uploaded.add(new FileUploadResponse(gFile.getId(), gFile.getName(), share));
                }
            }
        }
    }

    if (uploaded.isEmpty()) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print(gson.toJson(new ErrorResponse("Không có file nào được upload")));
    } else {
        out.print(gson.toJson(Collections.singletonMap("uploaded", uploaded)));
    }
    out.flush();
}


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        if (pathInfo == null || !pathInfo.startsWith("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(new ErrorResponse("File ID is required.")));
            return;
        }

        String fileId = pathInfo.substring(1);
        try {
            Drive driveService = GoogleDriveConfig.getDriveService();
            driveService.files().delete(fileId).execute();
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(new SuccessResponse("File deleted successfully.")));
        } catch (IOException e) {
            System.err.println("Deletion failed for file ID " + fileId + ": " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse("Deletion failed: " + e.getMessage())));
        }
        out.flush();
    }

    // Response classes for JSON serialization
    private static class ErrorResponse {
        String error;
        public ErrorResponse(String error) { this.error = error; }
    }

    private static class SuccessResponse {
        String message;
        public SuccessResponse(String message) { this.message = message; }
    }

    private static class FileUploadResponse {
        String id;
        String name;
        String shareLink;
        public FileUploadResponse(String id, String name, String shareLink) {
            this.id = id;
            this.name = name;
            this.shareLink = shareLink;
        }
    }

    private static class ShareLinkResponse {
        String shareLink;
        public ShareLinkResponse(String shareLink) { this.shareLink = shareLink; }
    }
}