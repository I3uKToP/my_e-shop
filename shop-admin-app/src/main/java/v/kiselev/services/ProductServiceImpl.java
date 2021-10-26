package v.kiselev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import v.kiselev.controller.DTO.ProductDto;
import v.kiselev.controller.ProductListParam;
import v.kiselev.persist.ProductRepository;
import v.kiselev.persist.ProductSpecifications;
import v.kiselev.persist.model.Picture;
import v.kiselev.persist.model.Product;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final PictureService pictureService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              PictureService pictureService) {
        this.productRepository = productRepository;

        this.pictureService = pictureService;
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
                        product.getBrand(),
                        product.getPictures().stream()
                                .map(picture -> picture.getId()).collect(Collectors.toList())
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
                        product.getBrand(),
                        product.getPictures().stream()
                                .map(picture -> picture.getId()).collect(Collectors.toList())));
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
                        product.getBrand(),
                        product.getPictures().stream()
                                .map(picture -> picture.getId()).collect(Collectors.toList())));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void save(ProductDto productDto) {
        Product product = (productDto.getId() != null) ? productRepository.findById(productDto.getId())
                .orElseThrow(() -> new RuntimeException("")) : new Product();

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setBrand(productDto.getBrand());

        if(productDto.getNewPictures() !=null) {
            for (MultipartFile newPicture : productDto.getNewPictures()) {
                try {
                    product.getPictures().add(new Picture(null,
                            newPicture.getOriginalFilename(),
                            newPicture.getContentType(),
                            pictureService.createPicture(newPicture.getBytes()),
                            product));
                } catch (IOException ex) {
                    throw  new RuntimeException(ex);
                }
            }
        }
        productRepository.save(product);
    }
}
