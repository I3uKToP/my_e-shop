package v.kiselev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import v.kiselev.controller.DTO.ProductDto;
import v.kiselev.controller.ProductListParam;
import v.kiselev.persist.model.Product;
import v.kiselev.persist.ProductRepository;
import v.kiselev.persist.ProductSpecifications;

import java.util.List;
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
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getBrand()
                )).collect(Collectors.toList());
    }

    @Override
    public Page<ProductDto> findWithFilter(ProductListParam productListParam) {

        Specification<Product> spec = Specification.where(null);

        if (productListParam.getProductName() != null && !productListParam.getProductName().isBlank()) {
            spec = spec.and(ProductSpecifications.productNamePrefix(productListParam.getProductName()));
        }
        if (productListParam.getMinPrice() != null) {
            spec = spec.and(ProductSpecifications.minPrice(productListParam.getMinPrice()));
        }
        if (productListParam.getMaxPrice() != null) {
            spec = spec.and(ProductSpecifications.maxPrice(productListParam.getMaxPrice()));
        }


        return productRepository.findAll(spec,
                PageRequest.of(Optional.ofNullable(productListParam.getPage()).orElse(1) - 1,
                        Optional.ofNullable(productListParam.getSize()).orElse(100),
                        Sort.by(Sort.Direction.fromString(Optional.ofNullable(productListParam.getDirectionSort()).orElse("asc")),
                                Optional.ofNullable(productListParam.getSortBy()).orElse("id"))))
                .map(product -> new ProductDto(product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getBrand()));
    }

    @Override
    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id)
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getBrand()
                ));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void save(ProductDto productDto) {
        Product product = new Product(
                productDto.getName(),
                productDto.getPrice(),
                productDto.getDescription(),
                productDto.getCategory(),
                productDto.getBrand());
        productRepository.save(product);
    }
}
