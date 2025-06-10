package Model.entity.auth;

import java.time.LocalDate;

public class Account {
    private String accId;
    private String password;
    private String email;
    private LocalDate regisDate;
    private String userId;
    private String verificationToken;
    private boolean isVerified;

    public Account() {}

    public Account(String email) {
        this.email = email;
    }

    public Account(String accId, String password, String email, LocalDate regisDate,
                    String userId, String verificationToken, boolean isVerified) {
        this.accId = accId;
        this.password = password;
        this.email = email;
        this.regisDate = regisDate;
        this.userId = userId;
        this.verificationToken = verificationToken;
        this.isVerified = isVerified;
    }

    public String getAccId() { return accId; }
    public void setAccId(String accId) { this.accId = accId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getRegisDate() { return regisDate; }
    public void setRegisDate(LocalDate regisDate) { this.regisDate = regisDate; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}
