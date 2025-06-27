package Service;

import Utils.AppConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CalShipFee {

    private String mapbox_api = new AppConfig().get("mapbox.public_token");
    private String direction_api = new AppConfig().get("directions_api");

    // Get coordinates from Mapbox Geocoding API
    public double[] getCoordinatesByMapbox(String address) throws Exception {
        String encoded = URLEncoder.encode(address, "UTF-8");
        String urlStr = "https://api.mapbox.com/geocoding/v5/mapbox.places/"
                + encoded + ".json?access_token=" + mapbox_api;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new IOException("Mapbox Geocoding API error");
        }

        StringBuilder response;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
        }

        // Get coordinates from the first feature
        String marker = "\"coordinates\":[";
        int idx = response.indexOf(marker);
        if (idx == -1) {
            throw new IOException("Coordinates not found in Mapbox response");
        }
        int start = idx + marker.length();
        int end = response.indexOf("]", start);
        String[] parts = response.substring(start, end).split(",");
        double lon = Double.parseDouble(parts[0].trim());
        double lat = Double.parseDouble(parts[1].trim());
        return new double[]{lat, lon};
    }

    public double getRouteDistance(double lat1, double lon1, double lat2, double lon2) throws Exception {
        String urlStr = "https://api.openrouteservice.org/v2/directions/driving-car";
        URL url = new URL(urlStr);

        String jsonInput = String.format("{\"coordinates\":[[%f,%f],[%f,%f]]}", lon1, lat1, lon2, lat2);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", direction_api);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int code = conn.getResponseCode();
        StringBuilder response = new StringBuilder();

        BufferedReader br;
        if (code == 200) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            // Đọc error stream để xem lỗi thực tế
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            String err = "";
            String line;
            while ((line = br.readLine()) != null) {
                err += line;
            }
            System.err.println("[Directions API Error] code=" + code + " response: " + err);
            throw new IOException("Directions API error: " + err);
        }

        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }
        br.close();

        // Parse distance
        String marker = "\"distance\":";
        int idx = response.indexOf(marker);
        if (idx == -1) {
            throw new IOException("Distance not found in response: " + response.toString());
        }
        int start = idx + marker.length();
        int end = response.indexOf(",", start);
        double distance = Double.parseDouble(response.substring(start, end));
        return distance / 1000.0; // km
    }

    public int calculateShipFee(double distanceKm) {
        // Mức phí mẫu tham khảo, bạn chỉnh theo nhu cầu
        int baseFee = 15000;        // phí cho 2km đầu tiên (VND)
        int extraFee_2_5 = 5000;    // mỗi km từ trên 2km đến 5km
        int extraFee_5plus = 4000;  // mỗi km trên 5km

        if (distanceKm <= 2) {
            return baseFee;
        } else if (distanceKm <= 5) {
            // Phí: 2km đầu (baseFee) + từng km tiếp theo (làm tròn lên)
            int extraKm = (int) Math.ceil(distanceKm - 2);
            return baseFee + extraKm * extraFee_2_5;
        } else {
            // Phí: 2km đầu + 3km tiếp theo + mỗi km tiếp nữa (làm tròn lên)
            int extraKm_2_5 = 3; // (từ 2 đến 5 là 3km)
            int extraKm_5plus = (int) Math.ceil(distanceKm - 5);
            return baseFee + extraKm_2_5 * extraFee_2_5 + extraKm_5plus * extraFee_5plus;
        }
    }

    public static void main(String[] args) {
        try {
            CalShipFee cal = new CalShipFee();

            // Địa chỉ cần tính (giữ nguyên dấu tiếng Việt)
            String address1 = "105-107 Đường Trần Đại Nghĩa, Hoà Hải, Ngũ Hành Sơn, Đà Nẵng 550000, Việt Nam";
            String address2 = "23 Trà Khê 9, Hoà Hải, Ngũ Hành Sơn, Đà Nẵng, Việt Nam";

            // Lấy toạ độ từ Mapbox Geocoding API
            double[] coord1 = cal.getCoordinatesByMapbox(address1);
            double[] coord2 = cal.getCoordinatesByMapbox(address2);

            // In toạ độ lấy được
            System.out.printf("Address 1 coordinates: %.6f, %.6f\n", coord1[0], coord1[1]);
            System.out.printf("Address 2 coordinates: %.6f, %.6f\n", coord2[0], coord2[1]);

            // Tính và in khoảng cách đường thực tế
            double realDistance = cal.getRouteDistance(coord1[0], coord1[1], coord2[0], coord2[1]);
            System.out.printf("Đường thực tế giữa hai địa chỉ: %.2f km\n", realDistance);
            int shipFee = cal.calculateShipFee(realDistance);
            System.out.printf("Phí ship dự kiến (giống Shopee): %,d VND\n", shipFee);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
