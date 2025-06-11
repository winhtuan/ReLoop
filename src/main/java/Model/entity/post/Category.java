package Model.entity.post;

public class Category {

    private int categoryId;
    private String name;
    private String slug;
    private int level;

    public Category() {
    }

    public Category(int categoryId, String name, String slug, int level) {
        this.categoryId = categoryId;
        this.name = name;
        this.slug = slug;
        this.level = level;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
