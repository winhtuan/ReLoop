package Controller.Auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import Utils.AppConfig;

public class GoogleLogin {

    private static AppConfig config = new AppConfig();

    public static String getToken(final String code) throws IOException {
        String response = Request.Post(config.get("google.link_get_token"))
                .bodyForm(Form.form()
                        .add("client_id", config.get("google.client_id"))
                        .add("client_secret", config.get("google.client_secret"))
                        .add("redirect_uri", config.get("google.redirect_uri"))
                        .add("code", code)
                        .add("grant_type", config.get("google.grant_type"))
                        .build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);

        // Kiểm tra xem access_token có trong phản hồi không
        if (!jobj.has("access_token")) {
            throw new RuntimeException("Không lấy được access_token, phản hồi: " + response);
        }

        return jobj.get("access_token").getAsString();
    }

    public static JsonObject getUserInfo(final String accessToken) throws IOException {
        String response = Request.Get(config.get("google.link_get_user_info") + accessToken)
                .execute().returnContent().asString();

        // Phân tích phản hồi JSON
        return new Gson().fromJson(response, JsonObject.class);
    }
}
