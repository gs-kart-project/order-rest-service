package com.gskart.order.data.repositories;

import com.gskart.order.data.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContactRepository extends JpaRepository<Contact, String> {
}
