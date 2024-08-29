package org.personal.washingmachine.enums;

import lombok.Getter;

/**
 * The order of the enum constants is important.
 * The order determines the recommended action based on the severity of damage,
 * with the first constant representing the least severe
 * and the last constant representing the most severe damage.
 */

@Getter
public enum Recommendation {
	NONE,
	REPACKAGE,
	RESALE,
	OUTLET,
	REPAIR,
	DISASSEMBLE
}
