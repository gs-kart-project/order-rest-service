package com.gskart.order.services;

import com.gskart.order.data.entities.DeliveryDetail;
import com.gskart.order.data.entities.Order;
import com.gskart.order.data.entities.PaymentDetail;
import com.gskart.order.data.repositories.IOrderRepository;
import com.gskart.order.security.models.GSKartResourceServerUserContext;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;
    private final GSKartResourceServerUserContext userContext;

    public OrderService(IOrderRepository orderRepository, GSKartResourceServerUserContext gsKartResourceServerUserContext) {
        this.orderRepository = orderRepository;
        this.userContext = gsKartResourceServerUserContext;
    }

    @Override
    public Order saveOrder(Order order) {
        order.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
        order.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
        order.setStatus(Order.Status.CREATED);

        order.getOrderedItems().forEach(orderedItem -> {
            orderedItem.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
            orderedItem.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
            orderedItem.getDeliveryDetails().forEach(deliveryDetail -> {
                deliveryDetail.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                deliveryDetail.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                deliveryDetail.setStatus(DeliveryDetail.Status.NOT_INITIATED);
                deliveryDetail.getContacts().forEach(contact -> {
                    contact.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                    contact.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());

                    contact.getAddresses().forEach(address -> {
                        address.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                        address.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                    });

                    contact.getPhoneNumbers().forEach(phoneNumber -> {
                        phoneNumber.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                        phoneNumber.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                    });
                });
            });
        });

        order.getPaymentDetails().forEach(paymentDetail -> {
            paymentDetail.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
            paymentDetail.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
            paymentDetail.setStatus(PaymentDetail.Status.INITIATED);

            paymentDetail.getBillContact().setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
            paymentDetail.getBillContact().setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
        });
        order = this.orderRepository.save(order);
        return order;
    }
}
