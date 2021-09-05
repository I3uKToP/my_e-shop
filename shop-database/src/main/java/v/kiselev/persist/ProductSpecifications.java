package v.kiselev.persist;

import org.springframework.data.jpa.domain.Specification;
import v.kiselev.persist.model.Product;

import java.math.BigDecimal;

public final class ProductSpecifications {

    public static Specification<Product> productNamePrefix(String prefix) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), prefix + "%"));
    }

    public static Specification<Product> minPrice(BigDecimal minPrice) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get("price"), minPrice));
    }

    public static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.le(root.get("price"), maxPrice));
    }
}
