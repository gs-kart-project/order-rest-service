package com.gskart.order.data.repositories;

import com.gskart.order.data.entities.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends CrudRepository<Order, String> {

    @Query("select o from orders o " +
            "join fetch o.orderedItems i " +
            "join fetch i.deliveryDetails where o.placedBy=(:placedBy)")
    List<Order> findAllByPlacedBy(String placedBy);

    @Query("select o from orders o " +
            "join fetch o.orderedItems i " +
            "join fetch i.deliveryDetails d " +
            "join fetch d.contacts c " +
            "join fetch o.paymentDetails pay " +
            "join fetch pay.billContact bc ")
    Optional<Order> findByOrderId(String orderId);
}
