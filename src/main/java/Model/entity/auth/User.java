package Model.entity.auth;

import java.math.BigDecimal;
import java.util.Date;

public class User {
    private String userId;
    private String fullName;
    private String role;
    private String address;
    private String phoneNumber;
    private String email;
    private boolean isPremium;
    private Date premiumExpiry;
    private BigDecimal balance;
    private String srcImg;

    // Default constructor with default role = "user"
    public User() {
        this.role = "user"; // Set default role to "user"
    }

    public User(String userId, String fullName, String role, String address, String phoneNumber, String email, boolean isPremium, Date premiumExpiry, BigDecimal balance, String srcImg) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isPremium = isPremium;
        this.premiumExpiry = premiumExpiry;
        this.balance = balance;
        this.srcImg = srcImg;
    }

    // All-args constructor
    public User(String userId, String fullName, String role, String address, String phoneNumber,
                String email, boolean isPremium, Date premiumExpiry, BigDecimal balance) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = (role == null || role.trim().isEmpty()) ? "user" : role; // Use default if role is blank
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isPremium = isPremium;
        this.premiumExpiry = premiumExpiry;
        this.balance = balance;
    }

    public boolean isIsPremium() {
        return isPremium;
    }

    public void setIsPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) {
        this.role = (role == null || role.trim().isEmpty()) ? "user" : role;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isPremium() { return isPremium; }
    public void setPremium(boolean premium) { isPremium = premium; }

    public Date getPremiumExpiry() { return premiumExpiry; }
    public void setPremiumExpiry(Date premiumExpiry) { this.premiumExpiry = premiumExpiry; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
