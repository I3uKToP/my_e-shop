package v.kiselev.controller;


import java.math.BigDecimal;


public class ProductListParam {

    private String productName;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Integer page;

    private Integer size;

    private String sortBy;

    private String directionSort;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getDirectionSort() {
        return directionSort;
    }

    public void setDirectionSort(String directionSort) {
        this.directionSort = directionSort;
    }
}
