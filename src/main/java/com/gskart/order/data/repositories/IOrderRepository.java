package com.gskart.order.data.repositories;

import com.gskart.order.data.entities.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends CrudRepository<Order, String> {
}
