package com.gskart.order.controllers;

import com.gskart.order.DTOs.requests.OrderRequest;
import com.gskart.order.DTOs.responses.ErrorResponse;
import com.gskart.order.DTOs.responses.OrderResponse;
import com.gskart.order.data.entities.Order;
import com.gskart.order.mappers.OrderMapper;
import com.gskart.order.services.IOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final IOrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(IOrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(OrderRequest orderRequest) {
        OrderResponse orderResponse = new OrderResponse();
        try {
            Order order = this.orderMapper.orderRequestToOrderEntity(orderRequest);
            order = this.orderService.saveOrder(order);
            orderResponse.setOrderId(order.getId());
            orderResponse.setStatus(order.getStatus().name());
            return ResponseEntity.ok(orderResponse);
        }
        catch (Exception ex){
            ex.printStackTrace();
            orderResponse.setStatus(ErrorResponse.Status.ORDER_NOT_PLACED.name());
            orderResponse.setMessage("Order could not be placed due to an exception.");
            return new ResponseEntity<>(orderResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
