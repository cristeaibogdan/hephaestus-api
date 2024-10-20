package org.personal.washingmachine.enums;

/**
 * The order of the enum constants is important.
 * The order determines the recommended action based on the severity of damage,
 * with the first constant representing the least severe
 * and the last constant representing the most severe damage.
 */

public enum Recommendation {
	NONE,
	REPACKAGE,
	RESALE,
	OUTLET,
	REPAIR,
	DISASSEMBLE
}