package com.gskart.order.controllers;

import com.gskart.order.DTOs.DeliveryDetailDto;
import com.gskart.order.DTOs.PaymentDetailDto;
import com.gskart.order.DTOs.requests.OrderRequest;
import com.gskart.order.DTOs.responses.ErrorResponse;
import com.gskart.order.DTOs.OrderDto;
import com.gskart.order.DTOs.responses.OrderPlacedResponse;
import com.gskart.order.data.entities.DeliveryDetail;
import com.gskart.order.data.entities.Order;
import com.gskart.order.data.entities.OrderedItem;
import com.gskart.order.data.entities.PaymentDetail;
import com.gskart.order.mappers.OrderMapper;
import com.gskart.order.services.IOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @Transactional
    public ResponseEntity<OrderPlacedResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        OrderPlacedResponse orderPlacedResponse = new OrderPlacedResponse();
        try {
            // 1. Save order details
            Order order = this.orderMapper.orderRequestToOrderEntity(orderRequest);
            order = this.orderService.saveOrder(order);

            // 2. Save delivery details by mapping ordered items list.
            List<DeliveryDetail> deliveryDetailList = new ArrayList<>();
            for (DeliveryDetailDto deliveryDetailDto : orderRequest.getDeliveryDetails()) {
                DeliveryDetail deliveryDetail = new DeliveryDetail();
                List<OrderedItem> orderedItems = new ArrayList<>();
                for(OrderedItem orderedItem : order.getOrderedItems()) {
                    if(deliveryDetailDto.getProductIds().contains(orderedItem.getProductId())) {
                        orderedItems.add(orderedItem);
                    }
                }
                deliveryDetail.setOrderedItems(orderedItems);
                deliveryDetail.setStatus(DeliveryDetail.Status.NOT_INITIATED);
                deliveryDetail.setContacts(deliveryDetailDto.getContacts().stream().map(orderMapper::ContactDtoToEntity).toList());
                deliveryDetailList.add(deliveryDetail);
            }
            orderService.saveDeliveryDetails(deliveryDetailList);

            // 3. Create order response
            orderPlacedResponse.setOrderId(order.getId());
            orderPlacedResponse.setStatus(order.getStatus().name());
            return ResponseEntity.ok(orderPlacedResponse);
        }
        catch (Exception ex){
            ex.printStackTrace();
            orderPlacedResponse.setStatus(ErrorResponse.Status.ORDER_NOT_PLACED.name());
            orderPlacedResponse.setMessage("Order could not be placed due to an exception.");
            return new ResponseEntity<>(orderPlacedResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderDto>> listOrdersForUserPlacedBy(@RequestParam String placedBy) {
        List<Order> ordersPlaced = this.orderService.getOrdersByUserPlacedBy(placedBy);
        List<OrderDto> ordersPlacedResponse =
            ordersPlaced.stream()
                .map(orderMapper::orderEntityToOrderDto)
                .toList();
        return new ResponseEntity<>(ordersPlacedResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String id) {
        Order order = this.orderService.getOrderById(id);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        OrderDto orderDto = orderMapper.orderEntityToOrderDto(order);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/{orderId}/updatePayment")
    public ResponseEntity<List<PaymentDetailDto>> updatePaymentDetails(@PathVariable String orderId, @RequestBody List<PaymentDetailDto> paymentDetailDtoList){
        List<PaymentDetail> paymentDetailList = paymentDetailDtoList.stream().map(orderMapper::paymentDetailDtoToEntity).toList();
        Order order = this.orderService.savePaymentDetails(orderId, paymentDetailList);
        paymentDetailList = order.getPaymentDetails();
        List<PaymentDetailDto> updatedPaymentDetailDtoList = paymentDetailList.stream().map(orderMapper::paymentDetailEntityToDto).toList();
        return ResponseEntity.ok(updatedPaymentDetailDtoList);
    }

}
