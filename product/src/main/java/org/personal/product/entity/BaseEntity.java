package org.personal.product.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@MappedSuperclass
abstract class BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
}
