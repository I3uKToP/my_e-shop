package v.kiselev.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import v.kiselev.controller.DTO.ProductDto;
import v.kiselev.persist.model.Product;
import v.kiselev.service.BrandService;
import v.kiselev.service.CategoryService;
import v.kiselev.service.ProductService;


import javax.validation.Valid;

@Controller
@RequestMapping("/product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @GetMapping
    public String listPage(Model model,
                           ProductListParam productListParam) {
        logger.info("Product list page requested");


        model.addAttribute("products", productService.findWithFilter(productListParam));
        return "products";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        logger.info("New product page requested");
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryService.findAll());
        model.addAttribute("brand", brandService.findAll());
        return "product_form";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        logger.info("Edit product");
        model.addAttribute("product", productService.findById(id));
        return "product_form";
    }

    @GetMapping("/remove/{id}")
    public String removeProduct(@PathVariable("id") Long id, Model model) {
        logger.info("Remove product");
        productService.deleteById(id);
        return "redirect:/product";
    }

    @PostMapping
    public String update(@Valid ProductDto productDto, BindingResult result) {
        logger.info("Saving product");
        if (result.hasErrors()) {
            return "product_form";
        }
        productService.save(productDto);
        return "redirect:/product";
    }
}
