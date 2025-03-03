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

	@Column(name = "category")
	String category;

	@Column(name = "manufacturer")
	String manufacturer;

	@Column(name = "model")
	String model;

	@Column(name = "type")
	String type;

	@Column(name = "serial_number")
	String serialNumber;

	public SolarPanel(String category, String manufacturer, String model, String type, String serialNumber) {
		this.category = category;
		this.manufacturer = manufacturer;
		this.model = model;
		this.type = type;
		this.serialNumber = serialNumber;
	}
}
