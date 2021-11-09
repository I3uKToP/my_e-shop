package v.kiselev.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import v.kiselev.controllers.DTO.OrderDto;
import v.kiselev.persist.model.OrderLineItem;
import v.kiselev.services.OrderService;
import v.kiselev.services.dto.LineItem;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public List<OrderDto> findAll(Authentication auth) {
        return orderService.findOrdersByUsername(auth.getName());
    }

    @PostMapping
    public void createOrder(Authentication auth) {
        orderService.createOrder(auth.getName());
    }

    @PostMapping(path = "/detail", produces = "application/json", consumes = "application/json")
    public List<OrderLineItem> showDetailsOrder(@RequestBody OrderDto orderDto) {

        return orderService.findOrdersLineItem(orderDto.getId());
    }


}
