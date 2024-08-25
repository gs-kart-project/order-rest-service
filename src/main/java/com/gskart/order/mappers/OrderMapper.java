package com.gskart.order.mappers;

import com.gskart.order.DTOs.*;
import com.gskart.order.DTOs.requests.OrderRequest;
import com.gskart.order.data.entities.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderMapper {
    public Order orderRequestToOrderEntity(OrderRequest orderRequest) {
        Order order = new Order();
        if(orderRequest.getOrderedItems()!=null){
            order.setOrderedItems(orderRequest.getOrderedItems().stream().map(this::orderedItemsDtoToEntity).toList());
        }
        order.setPlacedBy(orderRequest.getPlacedBy());
        order.setPlacedOn(orderRequest.getPlacedOn());
        order.setCartId(orderRequest.getCartId());
        if(orderRequest.getPaymentDetails() != null){
            order.setPaymentDetails(orderRequest.getPaymentDetails().stream().map(this::paymentDetailDtoToEntity).toList());
        }

        return order;
    }

    public List<DeliveryDetail> deliveryDetailEntityListFromOrderRequest(OrderRequest orderRequest) {
        if(orderRequest.getDeliveryDetails() == null){
            return null;
        }

        List<DeliveryDetail> deliveryDetails = new ArrayList<>();
        orderRequest.getDeliveryDetails().forEach(deliveryDetailDto -> {
            DeliveryDetail deliveryDetail = new DeliveryDetail();
            deliveryDetail.setStatus(DeliveryDetail.Status.NOT_INITIATED);
            deliveryDetail.setContacts(deliveryDetailDto.getContacts().stream().map(this::ContactDtoToEntity).toList());
            deliveryDetails.add(deliveryDetail);
        });
        return deliveryDetails;
    }

    public OrderDto orderEntityToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setPlacedOn(order.getPlacedOn());
        orderDto.setPlacedBy(order.getPlacedBy());
        List<DeliveryDetailDto> deliveryDetailDtoList = new ArrayList<>();

        if (order.getOrderedItems() != null) {
            orderDto.setOrderedItems(order.getOrderedItems().stream().map(orderedItem -> {
                OrderedItemDto orderedItemDto = new OrderedItemDto();
                orderedItemDto.setId(orderedItem.getId());
                orderedItemDto.setProductId(orderedItem.getProductId());
                orderedItemDto.setProductName(orderedItem.getProductName());
                orderedItemDto.setQuantityUnit(orderedItem.getQuantityUnit().name());
                orderedItemDto.setUnitPrice(orderedItem.getUnitPrice());
                orderedItemDto.setTotalPrice(orderedItem.getTotalPrice());
                orderedItemDto.setDescription(orderedItem.getDescription());
                orderedItemDto.setQuantity(orderedItem.getQuantity());
                if (orderedItem.getDeliveryDetails() != null) {
                    Optional<DeliveryDetailDto> deliveryDetailDtoOptional = deliveryDetailDtoList.stream()
                            .filter(dd->dd.getId().equals(orderedItem.getDeliveryDetails().getId()))
                            .findFirst();
                    if(deliveryDetailDtoOptional.isPresent()){
                        deliveryDetailDtoOptional.get().getProductIds().add(orderedItem.getProductId());
                    }
                    else{
                        DeliveryDetailDto deliveryDetailDto = new DeliveryDetailDto();
                        deliveryDetailDto.setId(orderedItem.getDeliveryDetails().getId());
                        deliveryDetailDto.setShippedOn(orderedItem.getDeliveryDetails().getShippedOn());
                        deliveryDetailDto.setDeliveredOn(orderedItem.getDeliveryDetails().getDeliveredOn());
                        deliveryDetailDto.setStatus(orderedItem.getDeliveryDetails().getStatus().name());
                        deliveryDetailDto.setProductIds(new ArrayList<>(){{add(orderedItem.getProductId());}});
                        if (orderedItem.getDeliveryDetails().getContacts() != null) {
                            deliveryDetailDto.setContacts(
                                    orderedItem.getDeliveryDetails().getContacts().stream().map(this::contactEntityToContactDto).toList()
                            );
                        }
                    }
                }
                return orderedItemDto;
            }).toList());
            orderDto.setDeliveryDetails(deliveryDetailDtoList);
        }
        if (order.getPaymentDetails() != null) {
            orderDto.setPaymentDetails(order.getPaymentDetails().stream().map(paymentDetail -> {
                PaymentDetailDto paymentDetailDto = new PaymentDetailDto();
                paymentDetailDto.setId(paymentDetail.getId());
                paymentDetailDto.setPaymentId(paymentDetail.getPaymentId());
                paymentDetailDto.setStatus(paymentDetail.getStatus().name());
                if (paymentDetail.getBillContact() != null) {
                    paymentDetailDto.setBillContact(contactEntityToContactDto(paymentDetail.getBillContact()));
                }
                return paymentDetailDto;
            }).toList());
        }
        return orderDto;
    }

    public ContactDto contactEntityToContactDto(Contact contact) {
        ContactDto contactDto = new ContactDto();
        contactDto.setId(contact.getId());
        contactDto.setFirstName(contact.getFirstName());
        contactDto.setLastName(contact.getLastName());
        contactDto.setEmailIds(contact.getEmailIds());
        if (contact.getPhoneNumbers() != null) {
            contactDto.setPhoneNumbers(
                    contact.getPhoneNumbers().stream().map(
                            phoneNumber -> {
                                PhoneNumberDto phoneNumberDto = new PhoneNumberDto();
                                phoneNumberDto.setId(phoneNumber.getId());
                                phoneNumberDto.setNumber(phoneNumber.getNumber());
                                phoneNumberDto.setType(phoneNumber.getType().name());
                                phoneNumberDto.setCountryCode(phoneNumber.getCountryCode());
                                return phoneNumberDto;
                            }
                    ).toList()
            );
        }
        if (contact.getAddresses() != null) {
            contactDto.setAddresses(
                    contact.getAddresses().stream().map(
                            address -> {
                                AddressDto addressDto = new AddressDto();
                                addressDto.setId(address.getId());
                                addressDto.setDoorNumber(address.getDoorNumber());
                                addressDto.setStreet(address.getStreet());
                                addressDto.setLine1(address.getLine1());
                                addressDto.setLine2(address.getLine2());
                                addressDto.setCity(address.getCity());
                                addressDto.setState(address.getState());
                                addressDto.setCountry(address.getCountry());
                                addressDto.setZip(address.getZip());
                                return addressDto;
                            }
                    ).toList()
            );
        }
        return contactDto;
    }

    public OrderedItem orderedItemsDtoToEntity(OrderedItemDto orderedItemDto){
        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setProductId(orderedItemDto.getProductId());
        orderedItem.setDescription(orderedItemDto.getDescription());
        orderedItem.setProductName(orderedItemDto.getProductName());
        orderedItem.setQuantity(orderedItemDto.getQuantity());
        orderedItem.setQuantityUnit(OrderedItem.QuantityUnit.valueOf(orderedItemDto.getQuantityUnit()));
        orderedItem.setUnitPrice(orderedItemDto.getUnitPrice());
        orderedItem.setTotalPrice(orderedItemDto.getTotalPrice());
        return orderedItem;
    }

    public Contact ContactDtoToEntity(ContactDto contactDto){
        Contact contact = new Contact();
        contact.setId(contactDto.getId());
        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setEmailIds(contactDto.getEmailIds());
        if (contactDto.getPhoneNumbers() != null) {
            contact.setPhoneNumbers(
                    contactDto.getPhoneNumbers().stream().map(
                            phoneNumberDto -> {
                                PhoneNumber phoneNumber = new PhoneNumber();
                                phoneNumber.setId(phoneNumberDto.getId());
                                phoneNumber.setNumber(phoneNumberDto.getNumber());
                                phoneNumber.setType(PhoneNumber.NumberType.valueOf(phoneNumberDto.getType()));
                                phoneNumber.setCountryCode(phoneNumberDto.getCountryCode());
                                return phoneNumber;
                            }
                    ).toList()
            );
        }
        if (contactDto.getAddresses() != null) {
            contact.setAddresses(
                    contactDto.getAddresses().stream().map(
                            addressDto -> {
                                Address address = new Address();
                                address.setId(addressDto.getId());
                                address.setDoorNumber(addressDto.getDoorNumber());
                                address.setStreet(addressDto.getStreet());
                                address.setLine1(addressDto.getLine1());
                                address.setLine2(addressDto.getLine2());
                                address.setCity(addressDto.getCity());
                                address.setState(addressDto.getState());
                                address.setCountry(addressDto.getCountry());
                                address.setZip(addressDto.getZip());
                                return address;
                            }
                    ).toList()
            );
        }
        return contact;
    }

    public PaymentDetail paymentDetailDtoToEntity(PaymentDetailDto paymentDetailDto){
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setPaymentId(paymentDetailDto.getPaymentId());
        paymentDetail.setBillContact(ContactDtoToEntity(paymentDetailDto.getBillContact()));
        paymentDetail.setStatus(PaymentDetail.Status.valueOf(paymentDetailDto.getStatus()));
        return paymentDetail;
    }

    public PaymentDetailDto paymentDetailEntityToDto(PaymentDetail paymentDetail){
        PaymentDetailDto paymentDetailDto = new PaymentDetailDto();
        paymentDetailDto.setId(paymentDetail.getId());
        paymentDetailDto.setPaymentId(paymentDetail.getPaymentId());
        paymentDetailDto.setBillContact(contactEntityToContactDto(paymentDetail.getBillContact()));
        paymentDetailDto.setStatus(paymentDetail.getStatus().name());
        return paymentDetailDto;
    }
}
