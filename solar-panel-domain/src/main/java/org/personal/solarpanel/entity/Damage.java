package org.personal.solarpanel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(schema = "public", name = "damage")
public class Damage extends BaseEntity {

	@Column(name = "hotSpots")
	private boolean hotSpots;

	@Column(name = "micro_cracks")
	private boolean microCracks;

	@Column(name = "snail_trails")
	private boolean snailTrails;

	@Column(name = "broken_glass")
	private boolean brokenGlass;

	@Column(name = "additional_details")
	private String additionalDetails;

	public Damage(boolean hotSpots, boolean microCracks, boolean snailTrails, boolean brokenGlass, String additionalDetails) {
		this.hotSpots = hotSpots;
		this.microCracks = microCracks;
		this.snailTrails = snailTrails;
		this.brokenGlass = brokenGlass;
		this.additionalDetails = additionalDetails;
	}

	public Damage(Damage damage) {
		this(
				damage.hotSpots,
				damage.microCracks,
				damage.snailTrails,
				damage.brokenGlass,
				damage.additionalDetails
		);
	}
}
