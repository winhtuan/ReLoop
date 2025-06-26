package Service;

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
}
