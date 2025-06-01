package org.personal.washingmachine.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.personal.washingmachine.enums.Recommendation;

import static org.personal.washingmachine.enums.Recommendation.*;

@Getter
@With
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class CostAssessment {

	@Column(name = "price")
	private int price;

	@Column(name = "repair_price")
	private int repairPrice;

	public Recommendation calculate() {

		if (this.price == 0 || this.repairPrice == 0) {
			return NONE;
		}

		boolean repairPriceExceedsHalfTheProductPrice = (this.repairPrice >= this.price * 0.5);

		return repairPriceExceedsHalfTheProductPrice
				? DISASSEMBLE
				: REPAIR;
	}
}
