package com.gskart.order.data.repositories;

import com.gskart.order.data.entities.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends CrudRepository<Order, String> {

    /**
     * Finds orders placed by user: {@code placedBy}. Eagerly loads ordered items and delivery details for each order
     * @param placedBy
     * @return
     */
    @Query("select o from orders o " +
            "join fetch o.orderedItems i " +
            "join fetch i.deliveryDetails where o.placedBy=(:placedBy)")
    List<Order> findAllByPlacedBy(String placedBy);


    /**
     * Gets order by id: {@code orderId}.Eager loading all dependencies using join fetch
     * @param orderId order Id
     * @return {@code Optional<Order>} Order if exists
     */
    @Query("select o from orders o " +
            "join fetch o.orderedItems i " +
            "join fetch i.deliveryDetails d " +
            "join fetch d.contacts c " +
//            "join fetch c.addresses a " +
//            "join fetch c.phoneNumbers p " +
            "join fetch o.paymentDetails pay " +
            "join fetch pay.billContact bc ") //+
//            "join fetch bc.addresses bca " +
//            "join fetch bc.phoneNumbers bcp")
    Optional<Order> findByOrderId(String orderId);
}
