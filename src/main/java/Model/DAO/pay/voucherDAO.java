package Model.DAO.pay;

import Model.entity.pay.Voucher;
import Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class voucherDAO {

    public List<Voucher> getVouchersByUser(String userId) {
        List<Voucher> list = new ArrayList<>();

        String sql = "SELECT v.* FROM user_vouchers uv " +
                     "JOIN vouchers v ON uv.voucher_id = v.voucher_id " +
                     "WHERE uv.user_id = ? AND uv.is_used = 0 AND v.is_active = 1 " +
                     "AND v.start_date <= NOW() AND v.end_date >= NOW()";

        try (Connection conn = DBUtils.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Voucher v = new Voucher();
                v.setVoucherId(rs.getString("voucher_id"));
                v.setCode(rs.getString("code"));
                v.setDescription(rs.getString("description"));
                v.setDiscountValue(rs.getInt("discount_value"));
                v.setMinOrderAmount(rs.getInt("min_order_amount"));
                v.setStartDate(rs.getDate("start_date"));
                v.setEndDate(rs.getDate("end_date"));
                v.setUsageLimit(rs.getInt("usage_limit"));
                v.setUsedCount(rs.getInt("used_count"));
                v.setActive(rs.getBoolean("is_active"));
                list.add(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public static void main(String[] args) {
        for(Voucher v:new voucherDAO().getVouchersByUser("CUS0001"))
        {
            System.out.println("ạdkasd"+v);
        }
    }
}
