package Controller.fileHandle;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/api/files")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class FilesServlet extends HttpServlet {

    private static final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        List<FileUploadResponse> uploaded = new ArrayList<>();

        for (Part part : req.getParts()) {
            if (!"file".equals(part.getName())) continue;
            if (part.getSubmittedFileName() == null || part.getSubmittedFileName().isBlank()) continue;

            try (InputStream inputStream = part.getInputStream()) {
                String url = S3Util.uploadFile(
                        part.getSubmittedFileName(),
                        inputStream,
                        part.getSize(),
                        part.getContentType()
                );
                uploaded.add(new FileUploadResponse(part.getSubmittedFileName(), url));
            } catch (Exception e) {
                e.printStackTrace();
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

    // Response classes for JSON serialization
    private static class ErrorResponse {
        String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }

    private static class FileUploadResponse {
        String name;
        String shareLink;
        public FileUploadResponse(String name, String shareLink) {
            this.name = name;
            this.shareLink = shareLink;
        }
    }
}
