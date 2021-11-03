package v.kiselev.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import v.kiselev.controllers.DTO.CategoryDto;
import v.kiselev.controllers.DTO.ProductDto;
import v.kiselev.persist.ProductRepository;
import v.kiselev.persist.model.Category;
import v.kiselev.persist.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


public class ProductServiceTest {

    private ProductService productService;

    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void testFindById() {
        Category expectedCategory = new Category();
        expectedCategory.setId(1L);
        expectedCategory.setName("Phone");

        Product expectedProduct = new Product();
        expectedProduct.setId(1L);
        expectedProduct.setName("Product name");
        expectedProduct.setCategory(expectedCategory);
        expectedProduct.setPictures(new ArrayList<>());
        expectedProduct.setPrice(new BigDecimal(80000));

        when(productRepository.findById(eq(expectedProduct.getId())))
                .thenReturn(Optional.of(expectedProduct));

        Optional<ProductDto> opt = productService.findById(expectedProduct.getId());

        assertTrue(opt.isPresent());
        assertEquals(expectedProduct.getId(), opt.get().getId());
        assertEquals(expectedProduct.getName(), opt.get().getName());
        assertEquals(expectedProduct.getPrice(), opt.get().getPrice());
        assertEquals(expectedProduct.getCategory().getName(), opt.get().getCategory().getName());
    }

    @Test
    public void testFindAll() {
        List<Long> pictures = new ArrayList<>();
        pictures.add(1L);
        pictures.add(2L);
        ProductDto productDto= new ProductDto(1L, "Apple", "Apple 12",
                new BigDecimal(80000),
                new CategoryDto(1L, "phone"),
                pictures);
        ProductDto productDto1 = new ProductDto(2L, "Xiaomi", "f2 pro",
                new BigDecimal(40000),
                new CategoryDto(1L, "phone"),
                pictures);

        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(productDto);
        productDtos.add(productDto1);

        Page<ProductDto> page = new PageImpl<ProductDto>(productDtos, Pageable.ofSize(5), productDtos.size());
        when(productRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);


        Page<ProductDto> productDtoPage = productService.findAll(Optional.of(1L), Optional.empty(),
                Optional.empty(), Optional.empty(), 1, 5, "abc");

//        assertTrue(productDtoPage.isEmpty());
        assertEquals(2, productDtoPage.getTotalElements());

    }
}