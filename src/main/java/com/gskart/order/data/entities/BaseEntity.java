package com.gskart.order.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 160)
    private String createdBy;
    private OffsetDateTime createdOn;

    @Column(length = 160)
    private  String modifiedBy;
    private OffsetDateTime modifiedOn;
}
