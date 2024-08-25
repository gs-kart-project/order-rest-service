package com.gskart.order.services;

import com.gskart.order.data.entities.*;
import com.gskart.order.data.repositories.IContactRepository;
import com.gskart.order.data.repositories.IDeliveryRepository;
import com.gskart.order.data.repositories.IOrderRepository;
import com.gskart.order.security.models.GSKartResourceServerUserContext;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;
    private final IDeliveryRepository deliveryRepository;
    private final IContactRepository contactRepository;
    private final GSKartResourceServerUserContext userContext;

    public OrderService(IOrderRepository orderRepository, IDeliveryRepository deliveryRepository, IContactRepository contactRepository, GSKartResourceServerUserContext gsKartResourceServerUserContext) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.contactRepository = contactRepository;
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
            if (orderedItem.getDeliveryDetails() != null) {
                orderedItem.getDeliveryDetails().setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                orderedItem.getDeliveryDetails().setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                orderedItem.getDeliveryDetails().setStatus(DeliveryDetail.Status.NOT_INITIATED);
                if (orderedItem.getDeliveryDetails().getContacts() != null) {
                    orderedItem.getDeliveryDetails().getContacts().forEach(contact -> {
                        contact.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                        contact.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                        if (contact.getAddresses() != null) {
                            contact.getAddresses().forEach(address -> {
                                address.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                                address.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                            });
                        }

                        if (contact.getPhoneNumbers() != null) {
                            contact.getPhoneNumbers().forEach(phoneNumber -> {
                                phoneNumber.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                                phoneNumber.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                            });
                        }
                    });
                }
            }
        });

        if (order.getPaymentDetails() != null) {
            order.getPaymentDetails().forEach(paymentDetail -> {
                paymentDetail.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                paymentDetail.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                paymentDetail.setStatus(PaymentDetail.Status.INITIATED);

                if (paymentDetail.getBillContact() != null) {
                    paymentDetail.getBillContact().setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                    paymentDetail.getBillContact().setCreatedBy(userContext.getGskartResourceServerUser().getUsername());

                    if (paymentDetail.getBillContact().getAddresses() != null) {
                        paymentDetail.getBillContact().getAddresses().forEach(address -> {
                            address.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                            address.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                        });
                    }

                    if (paymentDetail.getBillContact().getPhoneNumbers() != null) {
                        paymentDetail.getBillContact().getPhoneNumbers().forEach(phoneNumber -> {
                            phoneNumber.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                            phoneNumber.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                        });
                    }
                }
            });
        }

        order = this.orderRepository.save(order);
        return order;
    }

    @Override
    public Order savePaymentDetails(String orderId, List<PaymentDetail> paymentDetails) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            return null;
        }

        Order order = orderOptional.get();
        if (order.getPaymentDetails() == null) {
            order.setPaymentDetails(new ArrayList<>());
        }

        // Update paymentDetails
        Order finalOrder = order;
        paymentDetails.forEach(paymentDetail -> {
            Optional<PaymentDetail> existingPaymentDetailsOptional = finalOrder.getPaymentDetails().stream()
                    .filter(pd -> pd.getPaymentId().equals(paymentDetail.getPaymentId()))
                    .findFirst();
            if (existingPaymentDetailsOptional.isPresent()) {
                PaymentDetail existingPaymentDetail = existingPaymentDetailsOptional.get();
                existingPaymentDetail.setModifiedOn(OffsetDateTime.now(ZoneOffset.UTC));
                existingPaymentDetail.setModifiedBy(userContext.getGskartResourceServerUser().getUsername());
                existingPaymentDetail.setStatus(paymentDetail.getStatus());
                existingPaymentDetail.getBillContact().setType(paymentDetail.getBillContact().getType());
                existingPaymentDetail.getBillContact().setFirstName(paymentDetail.getBillContact().getFirstName());
                existingPaymentDetail.getBillContact().setLastName(paymentDetail.getBillContact().getLastName());
                existingPaymentDetail.getBillContact().setEmailIds(paymentDetail.getBillContact().getEmailIds());

                // Update Addresses


                if (paymentDetail.getBillContact().getAddresses() != null) {
                    if (existingPaymentDetail.getBillContact().getAddresses() == null) {
                        existingPaymentDetail.getBillContact().setAddresses(new ArrayList<>());
                    }
                    paymentDetail.getBillContact().getAddresses().forEach(address -> {
                        Optional<Address> existingAddressOptional = existingPaymentDetail.getBillContact().getAddresses().stream()
                                .filter(ad -> ad.getId().equals(address.getId()))
                                .findFirst();

                        if (existingAddressOptional.isPresent()) {
                            Address existingAddress = existingAddressOptional.get();
                            existingAddress.setModifiedOn(OffsetDateTime.now(ZoneOffset.UTC));
                            existingAddress.setModifiedBy(userContext.getGskartResourceServerUser().getUsername());
                            existingAddress.setDoorNumber(address.getDoorNumber());
                            existingAddress.setStreet(address.getStreet());
                            existingAddress.setLine1(address.getLine1());
                            existingAddress.setLine2(address.getLine2());
                            existingAddress.setCity(address.getCity());
                            existingAddress.setState(address.getState());
                            existingAddress.setZip(address.getZip());
                            existingAddress.setCountry(address.getCountry());
                            existingAddress.setType(address.getType());
                        } else {
                            address.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                            address.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                            paymentDetail.getBillContact().getAddresses().add(address);
                        }
                    });
                }


                // Update Phone numbers
                if (paymentDetail.getBillContact().getPhoneNumbers() != null) {
                    if (existingPaymentDetail.getBillContact().getPhoneNumbers() == null) {
                        existingPaymentDetail.getBillContact().setPhoneNumbers(new ArrayList<>());
                    }
                    paymentDetail.getBillContact().getPhoneNumbers().forEach(phoneNumber -> {
                        Optional<PhoneNumber> existingPhoneNumberOptional = existingPaymentDetail.getBillContact().getPhoneNumbers().stream()
                                .filter(ph -> ph.getId().equals(phoneNumber.getId()))
                                .findFirst();

                        if (existingPhoneNumberOptional.isPresent()) {
                            PhoneNumber existingPhoneNumber = existingPhoneNumberOptional.get();
                            existingPhoneNumber.setModifiedOn(OffsetDateTime.now(ZoneOffset.UTC));
                            existingPhoneNumber.setModifiedBy(userContext.getGskartResourceServerUser().getUsername());
                            existingPhoneNumber.setNumber(phoneNumber.getNumber());
                            existingPhoneNumber.setCountryCode(phoneNumber.getCountryCode());
                            existingPhoneNumber.setType(phoneNumber.getType());
                        } else {
                            phoneNumber.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                            phoneNumber.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                            existingPaymentDetail.getBillContact().getPhoneNumbers().add(phoneNumber);
                        }
                    });
                }

                existingPaymentDetail.getBillContact().setModifiedOn(OffsetDateTime.now(ZoneOffset.UTC));
                existingPaymentDetail.getBillContact().setModifiedBy(userContext.getGskartResourceServerUser().getUsername());
            } else {
                paymentDetail.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                paymentDetail.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                paymentDetail.setStatus(PaymentDetail.Status.INITIATED);

                paymentDetail.getBillContact().setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                paymentDetail.getBillContact().setCreatedBy(userContext.getGskartResourceServerUser().getUsername());

                paymentDetail.getBillContact().getAddresses().forEach(address -> {
                    address.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                    address.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                });

                paymentDetail.getBillContact().getPhoneNumbers().forEach(phoneNumber -> {
                    phoneNumber.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                    phoneNumber.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                });
                finalOrder.getPaymentDetails().add(paymentDetail);
            }
        });

        order.setModifiedOn(OffsetDateTime.now(ZoneOffset.UTC));
        order.setModifiedBy(userContext.getGskartResourceServerUser().getUsername());
        order = this.orderRepository.save(order);
        return order;
    }

    @Override
    public List<DeliveryDetail> saveDeliveryDetails(List<DeliveryDetail> deliveryDetails) {
        deliveryDetails.forEach(deliveryDetail -> {
            if(deliveryDetail.getContacts() != null){
                deliveryDetail.getContacts().forEach(contact -> {
                    contact.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                    contact.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                    if(contact.getAddresses() != null){
                        contact.getAddresses().forEach(address -> {
                            address.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                            address.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                        });
                    }
                    if(contact.getPhoneNumbers() != null){
                        contact.getPhoneNumbers().forEach(phoneNumber -> {
                            phoneNumber.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                            phoneNumber.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
                        });
                    }
                });

                deliveryDetail.setContacts(contactRepository.saveAll(deliveryDetail.getContacts()));
            }

            if (deliveryDetail.getId() == null) {
                deliveryDetail.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
                deliveryDetail.setCreatedBy(userContext.getGskartResourceServerUser().getUsername());
            } else {
                deliveryDetail.setModifiedOn(OffsetDateTime.now(ZoneOffset.UTC));
                deliveryDetail.setModifiedBy(userContext.getGskartResourceServerUser().getUsername());
            }
        });
        List<DeliveryDetail> deliveryDetailsSaved;
        deliveryDetailsSaved = deliveryRepository.saveAll(deliveryDetails);
        return deliveryDetailsSaved;
    }

    @Override
    public List<Order> getOrdersByUserPlacedBy(String userPlacedBy) {
        return this.orderRepository.findAllByPlacedBy(userPlacedBy);
    }

    @Override
    public Order getOrderById(String id) {
        Optional<Order> orderOptional = orderRepository.findByOrderId(id);
        return orderOptional.orElse(null);
    }
}
