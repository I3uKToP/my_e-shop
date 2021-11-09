package v.kiselev.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import v.kiselev.controllers.DTO.AddLineItemDto;
import v.kiselev.controllers.DTO.AllCartDto;
import v.kiselev.controllers.DTO.ProductDto;
import v.kiselev.services.CartService;
import v.kiselev.services.ProductService;
import v.kiselev.services.dto.LineItem;

import java.util.List;


@RequestMapping("/cart")
@RestController
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public List<LineItem> addToCart(@RequestBody AddLineItemDto addLineItemDto) {
        logger.info("New LineItem. ProductId = {}, qty = {}", addLineItemDto.getProductId(), addLineItemDto.getQty());

        ProductDto productDto = productService.findById(addLineItemDto.getProductId())
                .orElseThrow(RuntimeException::new);
        cartService.addProductQty(productDto, addLineItemDto.getColor(), addLineItemDto.getMaterial(), addLineItemDto.getQty());
        return cartService.getLineItems();
    }

    @GetMapping("/all")
    public AllCartDto findAll() {
        logger.info("All lineItems");
        return new AllCartDto(cartService.getLineItems(), cartService.getSubTotal());
    }

    //    @DeleteMapping(produces = "application/json", consumes = "application/json", value = MediaType.APPLICATION_JSON_VALUE)
//    @DeleteMapping(path = "/remove", produces = "application/json", consumes = "application/json")
//    @DeleteMapping(value = MediaType.APPLICATION_JSON_VALUE)
//    @RequestMapping(method = RequestMethod.DELETE, value = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(path = "/remove" ,produces = "application/json", consumes = "application/json")
    public AllCartDto removeItem(@RequestBody LineItem lineItem) {

        logger.info("Remove item from cart" + lineItem.getProductId());

        ProductDto productDto = productService.findById(lineItem.getProductId())
                .orElseThrow(RuntimeException::new);
        cartService.removeProduct(productDto, lineItem.getColor(), lineItem.getMaterial());

        return new AllCartDto(cartService.getLineItems(), cartService.getSubTotal());
    }

    @PostMapping(path = "/change" ,produces = "application/json", consumes = "application/json")
    public AllCartDto changeQty(@RequestBody LineItem lineItem) {

        logger.info("Change item qty from cart " + lineItem.getQty());

        ProductDto productDto = productService.findById(lineItem.getProductId())
                .orElseThrow(RuntimeException::new);
        cartService.removeProductQty(productDto, "", "", lineItem.getQty());

        return new AllCartDto(cartService.getLineItems(), cartService.getSubTotal());
    }

    @PostMapping(path = "/clear")
    public void clearCart() {
        logger.info("Clear cart");
        cartService.clearCart();
    }

}
