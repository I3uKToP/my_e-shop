package v.kiselev.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import v.kiselev.persist.model.Category;
import v.kiselev.service.CategoryService;

import javax.validation.Valid;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listPage(Model model) {
        logger.info("Category list page requested");

        model.addAttribute("category", categoryService.findAll());
        return "category";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        logger.info("New category page requested");
        model.addAttribute("category", new Category());
        return "category_form";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        logger.info("Edit product");
        model.addAttribute("product", categoryService.findById(id));
        return "category_form";
    }

    @GetMapping("/remove/{id}")
    public String removeProduct(@PathVariable("id") Long id, Model model) {
        logger.info("Remove category");
        categoryService.deleteById(id);
        return "redirect:/category";
    }

    @PostMapping
    public String update(@Valid Category category, BindingResult result) {
        logger.info("Saving category");
        if (result.hasErrors()) {
            return "category_form";
        }
        categoryService.save(category);
        return "redirect:/category";
    }
}
