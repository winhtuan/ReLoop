package Model.entity.post;

public class ProductAttributeValue {
    private int productAttributeValueId; // ID của bản ghi trong bảng product_attribute_value
    private String productId; // ID của sản phẩm liên kết
    private int attributeId; // ID của thuộc tính từ category_attribute
    private String value; // Giá trị của thuộc tính (ví dụ: màu sắc, kích thước, v.v.)

    public ProductAttributeValue() {}

    public ProductAttributeValue(int productAttributeValueId, String productId, int attributeId, String value) {
        this.productAttributeValueId = productAttributeValueId;
        this.productId = productId;
        this.attributeId = attributeId;
        this.value = value;
    }

    // Getters and Setters

    public int getProductAttributeValueId() {
        return productAttributeValueId;
    }

    public void setProductAttributeValueId(int productAttributeValueId) {
        this.productAttributeValueId = productAttributeValueId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}