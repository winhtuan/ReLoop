package Utils;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    private static AppConfig config = new AppConfig();

    public static java.sql.Connection getConnect() {
        try {
            Class.forName(config.get("db.driver"));
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver" + e);
        }
        try {
            java.sql.Connection con = DriverManager.getConnection(config.get("db.url"), config.get("db.user"), config.get("db.password"));
            return con;
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return null;
    }
    public static void main(String[] args) {
        System.out.println(getConnect());
    }
}
