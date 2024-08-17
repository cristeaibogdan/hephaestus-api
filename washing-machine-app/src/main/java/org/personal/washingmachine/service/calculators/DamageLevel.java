package org.personal.washingmachine.service.calculators;

import lombok.Getter;

@Getter
public enum DamageLevel {
	LEVEL_1(1, "REPACKAGE"),
	LEVEL_2(2, "RESALE"),
	LEVEL_3(3, "RESALE"),
	LEVEL_4(4, "REPAIR"),
	LEVEL_5(5, "DISASSEMBLE");

	private final int level;
	private final String recommendation;

	DamageLevel(int level, String recommendation) {
		this.level = level;
		this.recommendation = recommendation;
	}

	//TODO: Find a better implementation, or completly remove the need to have level in the DB
}
