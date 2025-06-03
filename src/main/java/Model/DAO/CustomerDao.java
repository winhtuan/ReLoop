package Model.DAO;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import Model.Entity.Customer;
import Utils.DBUtils;

public class CustomerDao{

    public static Customer getCustomerByID(int customerID) {
        Customer customer = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection con = DBUtils.getConnect()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(customerID);
                customer.setFullName(rs.getString("FullName"));
                customer.setAddress(rs.getString("Address"));
                customer.setPhoneNumber(rs.getString("PhoneNumber"));
                customer.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public static int addCustomer(String name, String email) {
        String query = "INSERT INTO users (FullName, email) VALUES (?,?)";
        int customerID = -1; // Giá trị mặc định nếu không lấy được ID

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, email);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = ps.getGeneratedKeys(); // Lấy khóa chính được tạo tự động
                if (rs.next()) {
                    customerID = rs.getInt(1);
                    System.out.println("Thêm khách hàng thành công! ID: " + customerID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm khách hàng: " + e);
        }

        return customerID;
    }
    
    public static int addCustomer(String address, String phoneNumber, String email) {
        String query = "INSERT INTO users (FullName, Address, PhoneNumber, email) VALUES (?,?, ?, ?)";
        int customerID = -1; // Giá trị mặc định nếu không lấy được ID

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, email);
            ps.setString(2, address);
            ps.setString(3, phoneNumber);
            ps.setString(4, email);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = ps.getGeneratedKeys(); // Lấy khóa chính được tạo tự động
                if (rs.next()) {
                    customerID = rs.getInt(1);
                    System.out.println("Thêm khách hàng thành công! ID: " + customerID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm khách hàng: " + e);
        }

        return customerID;
    }
    
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE users SET FullName=?, Address=?, PhoneNumber=?, email=? WHERE id=?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getEmail());
            ps.setInt(5, customer.getCustomerId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
     public static ArrayList<Customer> listAllCustomers() {
        ArrayList<Customer> list = new ArrayList<>();
        try (Connection con = DBUtils.getConnect()) {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM users");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("FullName"),
                        rs.getString("Address"),
                        rs.getString("PhoneNumber"),
                        rs.getString("email")
                ));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return list;
    }

    public static int newCustomer(Customer c) {
        int id = -1;
        String sql = "INSERT INTO users (FullName, Address, PhoneNumber, email) "
                + "OUTPUT INSERTED.id VALUES (?, ?, ?, ?)";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            // Gán giá trị cho các tham số
            stmt.setString(1, c.getFullName());
            stmt.setString(2, c.getAddress());
            stmt.setString(3, c.getPhoneNumber());
            stmt.setString(4, c.getEmail());

            // Thực thi truy vấn và lấy ID của bản ghi vừa thêm
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return id;
    }

    public boolean deleteCustomer(int customerID) {
        try (Connection conn = DBUtils.getConnect()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
            stmt.setInt(1, customerID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log lỗi ra console
            return false;
        }
    }


    public Customer getCustomerById(int customerID) {
        Customer customer = null;

        try (Connection conn = DBUtils.getConnect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("FullName"),
                        rs.getString("Address"),
                        rs.getString("PhoneNumber"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log lỗi ra console
        }
        return customer;
    }
}