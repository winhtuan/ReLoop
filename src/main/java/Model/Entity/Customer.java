/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Model.Entity;


public class Customer {
    private int customerId;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;

    // Default constructor
    public Customer() {
    }

    // Parameterized constructor
    public Customer(int customerId, String fullName, String address, String phoneNumber, String email) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" + "customerId=" + customerId + ", fullName=" + fullName + ", address=" + address + ", phoneNumber=" + phoneNumber + ", email=" + email + '}';
    }
    
    
}
