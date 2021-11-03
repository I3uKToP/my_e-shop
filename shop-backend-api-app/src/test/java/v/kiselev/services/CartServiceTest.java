package v.kiselev.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import v.kiselev.controllers.DTO.ProductDto;
import v.kiselev.services.dto.LineItem;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    public void init() {
        cartService = new CartServiceImpl();
    }

    @Test
    public void testIfNewCartIsEmpty() {
        assertNotNull(cartService.getLineItems());
        assertEquals(0, cartService.getLineItems().size());
        assertEquals(BigDecimal.ZERO, cartService.getSubTotal());
    }

    @Test
    public void testAddProduct() {
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(80));
        expectedProduct.setName("Apple");

        cartService.addProductQty(expectedProduct, "color", "material", 2);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(1, lineItems.size());

        LineItem lineItem = lineItems.get(0);
        assertEquals("color", lineItem.getColor());
        assertEquals("material", lineItem.getMaterial());
        assertEquals(2, lineItem.getQty());

        assertEquals(expectedProduct.getId(), lineItem.getProductId());
        assertNotNull(lineItem.getProductDto());
        assertEquals(expectedProduct.getName(), lineItem.getProductDto().getName());
    }

    @Test
    public void testRemoveProductQty() {
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(80));
        expectedProduct.setName("Apple");

        cartService.addProductQty(expectedProduct, "color", "material", 2);

        cartService.removeProductQty(expectedProduct, "color", "material", 1);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(1, lineItems.get(0).getQty());
    }

    @Test
    public void testRemoveProduct() {
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(80));
        expectedProduct.setName("Apple");

        cartService.addProductQty(expectedProduct, "color", "material", 2);

        cartService.removeProduct(expectedProduct, "color", "material");

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(0, lineItems.size());

        cartService.addProductQty(expectedProduct, "color_1", "material_1", 3);
        cartService.addProductQty(expectedProduct, "color", "material", 2);

        cartService.removeProduct(expectedProduct, "color_1", "material_1");

        lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(1, lineItems.size());
        assertEquals("color", lineItems.get(0).getColor());
        assertEquals(2, lineItems.get(0).getQty());
    }

    @Test
    public void testClearCart() {
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(80));
        expectedProduct.setName("Apple");

        cartService.addProductQty(expectedProduct, "color", "material", 2);
        cartService.addProductQty(expectedProduct, "color_1", "material_1", 3);
        cartService.clearCart();

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(0, lineItems.size());
    }

    @Test
    public void testGetSubTotal() {
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(80));
        expectedProduct.setName("Apple");

        cartService.addProductQty(expectedProduct, "color", "material", 2);
        cartService.addProductQty(expectedProduct, "color_1", "material_1", 3);

        assertEquals(new BigDecimal(400), cartService.getSubTotal());
    }
}
