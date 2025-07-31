package Model.entity.post;

import java.io.Serializable;

public class CategoryAttribute implements Serializable {
    private Integer attributeId; // ánh xạ attr_id
    private Integer categoryId; // ánh xạ category_id
    private String name; // ánh xạ name
    private String inputType; // ánh xạ input_type
    private String options; // ánh xạ options
    private Boolean isRequired; // ánh xạ is_required

    // Constructors
    public CategoryAttribute() {}

    public CategoryAttribute(Integer attributeId, Integer categoryId, String name, String inputType, String options, Boolean isRequired) {
        this.attributeId = attributeId;
        this.categoryId = categoryId;
        this.name = name;
        this.inputType = inputType;
        this.options = options;
        this.isRequired = isRequired;
    }

    // Getters and Setters
    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputType() { // Getter phải là getInputType()
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

}