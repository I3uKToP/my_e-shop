package v.kiselev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import v.kiselev.controllers.DTO.CategoryDto;
import v.kiselev.controllers.DTO.ProductDto;
import v.kiselev.persist.ProductRepository;

import v.kiselev.persist.ProductSpecifications;
import v.kiselev.persist.model.Picture;
import v.kiselev.persist.model.Product;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductDto> findAll(Optional<Long> categoryId, Optional<String> namePattern,
                                    Optional<Integer> minPrice, Optional<Integer> maxPrice,
                                    Integer page, Integer size, String sortField) {
        Specification<Product> spec = Specification.where(null);
        if (categoryId.isPresent() && categoryId.get() != -1) {
            spec = spec.and(ProductSpecifications.byCategory(categoryId.get()));
        }
        if (namePattern.isPresent()) {
            spec = spec.and(ProductSpecifications.byName(namePattern.get()));
        }
        if(minPrice.isPresent()) {
            spec = spec.and(ProductSpecifications.minPrice(new BigDecimal(minPrice.get())));
        }
        if(maxPrice.isPresent()) {
            spec = spec.and(ProductSpecifications.maxPrice(new BigDecimal(maxPrice.get())));
        }
        return productRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sortField)))
                .map(product -> new ProductDto(product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        new CategoryDto(product.getCategory().getId(), product.getCategory().getName()),
                        product.getPictures().stream()
                                .map(Picture::getId)
                                .collect(Collectors.toList())));
    }

    @Override
    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id)
                .map(product -> new ProductDto(product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        new CategoryDto(product.getCategory().getId(), product.getCategory().getName()),
                        product.getPictures().stream()
                                .map(Picture::getId)
                                .collect(Collectors.toList())));
    }
}
