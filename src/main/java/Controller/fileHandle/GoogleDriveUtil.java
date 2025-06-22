/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.fileHandle;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import java.io.IOException;
import java.util.List;

public class GoogleDriveUtil {

    private static String uploadRootFolderId = null;

    public static String ensureUploadRootFolder() throws IOException {
        if (uploadRootFolderId != null) {
            return uploadRootFolderId;
        }

        Drive driveService = GoogleDriveConfig.getDriveService();
        String query = "mimeType='application/vnd.google-apps.folder' and name='UploadServer' and trashed=false";
        FileList result = driveService.files().list()
                .setQ(query)
                .setFields("files(id, name)")
                .setSpaces("drive")
                .execute();

        List<File> files = result.getFiles();
        if (files != null && !files.isEmpty()) {
            uploadRootFolderId = files.get(0).getId();
            return uploadRootFolderId;
        } else {
            // Tạo thư mục nếu không tìm thấy
            File fileMetadata = new File();
            fileMetadata.setName("UploadServer");
            fileMetadata.setMimeType("application/vnd.google-apps.folder");
            File folder = driveService.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            uploadRootFolderId = folder.getId();
            return uploadRootFolderId;
        }
    }

    public static String getOrCreateShareLink(String fileId) throws IOException {
        Drive driveService = GoogleDriveConfig.getDriveService();
        try {
            List<Permission> permissions = driveService.permissions().list(fileId).execute().getPermissions();
            for (Permission p : permissions) {
                if ("anyone".equals(p.getType()) && "reader".equals(p.getRole())) {
                    return "https://drive.google.com/uc?export=view&id=" + fileId;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking existing permissions, creating new: " + e.getMessage());
        }

        // Nếu chưa có quyền "anyone", thì thêm
        Permission newPermission = new Permission()
                .setType("anyone")
                .setRole("reader");
        driveService.permissions().create(fileId, newPermission).execute();

        // Trả về link nhúng
        return "https://drive.google.com/uc?export=view&id=" + fileId;
    }

}
