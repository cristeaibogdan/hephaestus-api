package org.personal.solarpanel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(schema = "public", name = "solar_panel")
public class SolarPanel extends BaseEntity {

	@Column(name = "serial_number")
	String serialNumber;

	public SolarPanel(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}
