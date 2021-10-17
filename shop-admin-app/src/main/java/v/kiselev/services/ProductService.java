package v.kiselev.services;

import org.springframework.data.domain.Page;
import v.kiselev.controller.DTO.ProductDto;
import v.kiselev.controller.ProductListParam;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductDto> findAll();

    Page<ProductDto> findWithFilter(ProductListParam productListParam);

    Optional<ProductDto> findById(Long id);

    void deleteById(Long id);

    void save(ProductDto productDto);
}
