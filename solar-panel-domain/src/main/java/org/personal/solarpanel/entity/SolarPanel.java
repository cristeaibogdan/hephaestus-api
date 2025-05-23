package org.personal.solarpanel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.personal.solarpanel.enums.Recommendation;

import java.time.LocalDateTime;

import static lombok.AccessLevel.*;

@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(schema = "public", name = "solar_panel")
public class SolarPanel extends BaseEntity {

	@Column(name = "category")
	private String category;

	@Column(name = "manufacturer")
	private String manufacturer;

	@Column(name = "model")
	private String model;

	@Column(name = "type")
	private String type;

	@Column(name = "serial_number")
	private String serialNumber;

	@Setter(NONE)
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Setter(NONE)
	@Enumerated(EnumType.STRING)
	@Column(name = "recommendation")
	private Recommendation recommendation;

	@Getter(NONE) @Setter(NONE)
	@JoinColumn(name = "damage_id")
	@OneToOne(cascade = CascadeType.ALL)
	private Damage damage;

	public SolarPanel(String category, String manufacturer, String model, String type, String serialNumber, Damage damage) {
		this.category = category;
		this.manufacturer = manufacturer;
		this.model = model;
		this.type = type;
		this.serialNumber = serialNumber;
		this.damage = damage;
		this.recommendation = this.damage.calculate();
	}

	public Damage getDamage() {
		return new Damage(this.damage);
	}

	public SolarPanel setDamage(Damage damage) {
		this.damage = new Damage(damage);
		this.recommendation = this.damage.calculate();
		return this;
	}
}
