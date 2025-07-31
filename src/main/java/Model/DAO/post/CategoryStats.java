/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO.post;

/**
 *
 * @author ACER
 */
public class CategoryStats {

    private String categoryName;
    private int totalProducts;

    public CategoryStats(String categoryName, int totalProducts) {
        this.categoryName = categoryName;
        this.totalProducts = totalProducts;
    }

    // Getters
    public String getCategoryName() {
        return categoryName;
    }

    public int getTotalProducts() {
        return totalProducts;
    }
}
