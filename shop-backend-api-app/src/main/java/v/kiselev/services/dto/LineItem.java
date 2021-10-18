package v.kiselev.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import v.kiselev.controllers.DTO.ProductDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class LineItem implements Serializable {

    private Long productId;

    private ProductDto productDto;

    private Integer qty;

    private String material;

    private String color;

    private Integer itemTotal;

    public LineItem() {
    }

    public LineItem(ProductDto productDto, String color, String material, Integer itemTotal) {
        this.productId = productDto.getId();
        this.productDto = productDto;
        this.color = color;
        this.material = material;
        this.itemTotal = itemTotal;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getItemTotal() {
        return productDto.getPrice().multiply(new BigDecimal(qty));
    }

    public void setItemTotal(Integer itemTotal) {
        this.itemTotal = itemTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return Objects.equals(productId, lineItem.productId) &&
                Objects.equals(material, lineItem.material) &&
                Objects.equals(color, lineItem.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, material, color);
    }
}
