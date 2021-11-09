package v.kiselev.services;


import org.springframework.data.domain.Page;
import v.kiselev.controllers.DTO.ProductDto;

import java.util.Optional;

public interface ProductService {

    Page<ProductDto> findAll(Optional<Long> categoryId, Optional<String> namePattern,
                             Optional<Integer> minPrice, Optional<Integer> maxPrice,
                             Integer page, Integer size, String sortField);

    Optional<ProductDto> findById(Long id);

}
