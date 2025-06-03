package Model.Entity;

import java.time.LocalDate;

public class Account {
    private int accId;
    private String password;
    private String email;
    private String role;
    private LocalDate regisDate;
    private int userId;  // tương ứng userid trong DB
    private String verificationToken;
    private boolean isVerified;
    private int numberPost;

    public Account() {
    }

    // Constructor đầy đủ
    public Account(int accId, String password, String email, String role, LocalDate regisDate, int userId,
                   String verificationToken, boolean isVerified, int numberPost) {
        this.accId = accId;
        this.password = password;
        this.email = email;
        this.role = role;
        this.regisDate = regisDate;
        this.userId = userId;
        this.verificationToken = verificationToken;
        this.isVerified = isVerified;
        this.numberPost = numberPost;
    }

    public Account(String email) {
        this.email = email;
    }

    // Getters và setters
    public int getAccId() { return accId; }
    public void setAccId(int accId) { this.accId = accId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getRegisDate() { return regisDate; }
    public void setRegisDate(LocalDate regisDate) { this.regisDate = regisDate; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public int getNumberPost() { return numberPost; }
    public void setNumberPost(int numberPost) { this.numberPost = numberPost; }

    @Override
    public String toString() {
        return "Account{" +
                "accId=" + accId +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", regisDate=" + regisDate +
                ", userId=" + userId +
                ", verificationToken='" + verificationToken + '\'' +
                ", isVerified=" + isVerified +
                ", numberPost=" + numberPost +
                '}';
    }
}
