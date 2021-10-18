package v.kiselev.services;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import v.kiselev.controllers.DTO.ProductDto;
import v.kiselev.services.dto.LineItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartServiceImpl implements CartService{

    private final Map<LineItem, Integer> lineItems = new HashMap<>();

    @Override
    public void addProductQty(ProductDto productDto, String color, String material, int qty) {
        LineItem lineItem = new LineItem(productDto,color, material, productDto.getPrice().intValue());
        lineItems.put(lineItem, lineItems.getOrDefault(lineItem, 0) + qty);
    }

    @Override
    public void removeProductQty(ProductDto productDto, String color, String material, int qty) {
        LineItem lineItem = new LineItem(productDto,color, material, productDto.getPrice().intValue());
        lineItems.put(lineItem, qty);
    }

    @Override
    public void removeProduct(ProductDto productDto, String color, String material) {
        lineItems.remove(new LineItem(productDto, color, material, productDto.getPrice().intValue()));

    }

    @Override
    public void clearCart() {
        lineItems.clear();
    }

    @Override
    public List<LineItem> getLineItems() {
        lineItems.forEach(LineItem::setQty);
        return new ArrayList<>(lineItems.keySet());
    }

    @Override
    public BigDecimal getSubTotal() {
        lineItems.forEach(LineItem::setQty);
        return lineItems.keySet()
                .stream().map(LineItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
