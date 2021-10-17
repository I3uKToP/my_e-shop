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
import v.kiselev.persist.model.Brand;
import v.kiselev.services.BrandService;

import javax.validation.Valid;

@Controller
@RequestMapping("/brand")
public class BrandController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }


    @GetMapping
    public String listPage(Model model) {
        logger.info("Brand list page requested");

        model.addAttribute("brand", brandService.findAll());
        return "brand";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        logger.info("New brand page requested");
        model.addAttribute("brand", new Brand());
        return "brand_form";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        logger.info("Edit brand");
        model.addAttribute("brand", brandService.findById(id));
        return "brand_form";
    }

    @GetMapping("/remove/{id}")
    public String removeProduct(@PathVariable("id") Long id) {
        logger.info("Remove brand");
        brandService.deleteById(id);
        return "redirect:/brand";
    }

    @PostMapping
    public String update(@Valid Brand brand, BindingResult result) {
        logger.info("Saving brand");
        if (result.hasErrors()) {
            return "brand_form";
        }
        brandService.save(brand);
        return "redirect:/brand";
    }
}
