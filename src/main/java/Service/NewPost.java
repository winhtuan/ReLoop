package Service;

import Model.DAO.auth.UserDao;
import Model.entity.auth.User;
import Model.entity.post.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewPost {

    public static int[] getPageRange(int currentPage, int totalPages, int maxPagesToShow) {
        int startPage = 1;
        int endPage = totalPages;
        if (totalPages > maxPagesToShow) {
            startPage = currentPage - maxPagesToShow / 2;
            if (startPage < 1) {
                startPage = 1;
            }
            endPage = startPage + maxPagesToShow - 1;
            if (endPage > totalPages) {
                endPage = totalPages;
                startPage = endPage - maxPagesToShow + 1;
                if (startPage < 1) {
                    startPage = 1;
                }
            }
        }
        return new int[]{startPage, endPage};
    }

    public static List<Product> filterPriorityPost(List<Product> list) {
        List<String> userPremium = new UserDao().getListUserPremium();

        List<Product> premiumProducts = new ArrayList<>();
        List<Product> normalProducts = new ArrayList<>();

        // Đảo danh sách gốc để ưu tiên bài đăng mới
        List<Product> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);

        for (Product p : reversed) {
            if (userPremium.contains(p.getUserId())) {
                premiumProducts.add(p);
            } else {
                normalProducts.add(p);
            }
        }

        // Gộp danh sách: premium lên trước
        List<Product> result = new ArrayList<>();
        result.addAll(premiumProducts);
        result.addAll(normalProducts);

        return result;
    }

}
