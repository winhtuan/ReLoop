/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.fileHandle;

import Utils.AppConfig;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleDriveConfig {

    private static final String CLIENT_ID = new AppConfig().get("driver.clientID"); // Thay bằng Client ID của bạn
    private static final String CLIENT_SECRET = new AppConfig().get("driver.clientSecret"); // Thay bằng Client Secret của bạn
    private static final String REFRESH_TOKEN = new AppConfig().get("driver.refreshID"); // Thay bằng Refresh Token của bạn
    private static final String REDIRECT_URI = "https://developers.google.com/oauthplayground"; // Hoặc URL redirect của bạn

    private static HttpTransport HTTP_TRANSPORT;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Drive getDriveService() throws IOException {
        Credential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .build()
                .setRefreshToken(REFRESH_TOKEN);

        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Mini Google Drive Java")
                .build();
    }
}
