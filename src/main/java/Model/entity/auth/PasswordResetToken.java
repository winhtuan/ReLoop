package Model.entity.auth;

import java.util.Date;

public class PasswordResetToken {
    private String resetId;
    private String userId;
    private String email;
    private String token;
    private Date expiryDate;

    public PasswordResetToken() {}

    public PasswordResetToken(String resetId, String userId, String email, String token, Date expiryDate) {
        this.resetId = resetId;
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public String getResetId() { return resetId; }
    public void setResetId(String resetId) { this.resetId = resetId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
}
