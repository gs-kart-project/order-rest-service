package com.gskart.order.data.repositories;

import com.gskart.order.data.entities.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDeliveryRepository extends JpaRepository<DeliveryDetail, String> {

}
