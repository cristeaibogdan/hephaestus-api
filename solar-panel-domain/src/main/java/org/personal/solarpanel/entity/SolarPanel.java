package org.personal.solarpanel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(schema = "public", name = "solar_panel")
public class SolarPanel extends BaseEntity {

	@Column(name = "serial_number")
	String serialNumber;

	public SolarPanel(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}
