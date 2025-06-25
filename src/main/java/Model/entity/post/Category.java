package Model.entity.post;

public class Category {

    private int categoryId;
    private String name;
    private int parent_id;
    private String slug;
    private int level;

    public Category() {
    }

    public Category(int categoryId, String name, int parent_id, String slug, int level) {
        this.categoryId = categoryId;
        this.name = name;
        this.parent_id = parent_id;
        this.slug = slug;
        this.level = level;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
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

