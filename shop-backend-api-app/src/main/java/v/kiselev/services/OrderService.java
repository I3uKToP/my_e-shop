package v.kiselev.services;

import v.kiselev.controllers.DTO.OrderDto;
import v.kiselev.persist.model.OrderLineItem;

import java.util.List;


public interface OrderService {

    List<OrderDto> findOrdersByUsername(String username);

    void createOrder(String username);

    List<OrderLineItem> findOrdersLineItem(Long id);
}
