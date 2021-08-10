package v.kiselev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import v.kiselev.controller.ProductListParam;
import v.kiselev.persist.Product;
import v.kiselev.persist.ProductRepository;
import v.kiselev.persist.ProductSpecifications;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findWithFilter(ProductListParam productListParam) {

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
                                Optional.ofNullable(productListParam.getSortBy()).orElse("id"))));
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }
}
