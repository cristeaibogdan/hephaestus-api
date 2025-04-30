package org.personal.solarpanel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.solarpanel.enums.Recommendation;

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
				damage.isHotSpots(),
				damage.isMicroCracks(),
				damage.isSnailTrails(),
				damage.isBrokenGlass(),
				damage.getAdditionalDetails()
		);
	}

	public Recommendation calculate() {
		int count = countFlags();
		return getRecommendation(count);
	}

	private int countFlags(){
		int result = 0;
		if (this.hotSpots) { result++; }
		if (this.microCracks) { result++; }
		if (this.snailTrails) { result++; }
		if (this.brokenGlass) { result++; }
		return result;
	}

	private Recommendation getRecommendation(int count) {
		return switch (count) {
			case 1, 2 -> Recommendation.REPAIR;
			case 3 -> Recommendation.RECYCLE;
			case 4 -> Recommendation.DISPOSE;
			default -> throw new CustomException("Invalid recommendation issued", ErrorCode.GENERAL);
		};
	}
}
