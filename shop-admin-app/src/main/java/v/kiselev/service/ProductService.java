package v.kiselev.service;

import org.springframework.data.domain.Page;
import persist.Product;
import v.kiselev.controller.ProductListParam;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Page<Product> findWithFilter(ProductListParam productListParam);

    Optional<Product> findById(Long id);

    void deleteById(Long id);

    void save(Product product);
}
