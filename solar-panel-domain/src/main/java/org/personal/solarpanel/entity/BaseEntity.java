package org.personal.solarpanel.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@MappedSuperclass
abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
}
