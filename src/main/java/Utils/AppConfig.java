package Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final Properties properties = new Properties();

    public AppConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Configuration file 'config.properties' not found in classpath.");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        AppConfig config = new AppConfig();

        String clientId = config.get("google.client_id");
        String dbPassword = config.get("db.password");

        System.out.println("Google Client ID: " + clientId);
        System.out.println("Database Password: " + dbPassword);
    }

}
