package org.personal.washingmachine.entity.embedded;

import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.personal.washingmachine.enums.Recommendation;

import java.util.Arrays;
import java.util.Collections;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.personal.washingmachine.enums.Recommendation.*;
import static org.personal.washingmachine.enums.Recommendation.NONE;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class HiddenSurfaceDamage implements Damage {
	private static final int HIDDEN_SURFACES_THRESHOLD = 7;

	@Column(name = "hidden_surfaces_scratches_length")
	private double hiddenSurfacesScratchesLength;

	@Column(name = "hidden_surfaces_dents_depth")
	private double hiddenSurfacesDentsDepth;

	@Column(name = "hidden_surfaces_minor_damage")
	private String hiddenSurfacesMinorDamage;

	@Column(name = "hidden_surfaces_major_damage")
	private String hiddenSurfacesMajorDamage;

	public boolean hasScratches() {
		return hiddenSurfacesScratchesLength > 0;
	}

	public boolean hasNoScratches() {
		return !hasScratches();
	}

	public boolean hasDents() {
		return hiddenSurfacesDentsDepth > 0;
	}

	public boolean hasNoDents() {
		return !hasDents();
	}

	public boolean hasMinorDamage() {
		return isNotBlank(hiddenSurfacesMinorDamage);
	}

	public boolean hasMajorDamage() {
		return isNotBlank(hiddenSurfacesMajorDamage);
	}

	@Override
	public boolean isApplicable() {
		return hasScratches() ||
				hasDents() ||
				hasMinorDamage() ||
				hasMajorDamage();
	}

	@Override
	public Recommendation calculate() {
		if (isNotApplicable()) {
			return NONE;
		}

		Recommendation recommendationForScratches = calculateForScratches();
		Recommendation recommendationForDents = calculateForDents();
		Recommendation recommendationForMinorDamage = calculateForMinorDamage();
		Recommendation recommendationForMajorDamage = calculateForMajorDamage();

		return Collections.max(Arrays.asList(
				recommendationForScratches,
				recommendationForDents,
				recommendationForMinorDamage,
				recommendationForMajorDamage
		));
	}

	@VisibleForTesting
	Recommendation calculateForScratches() {
		if (hasNoScratches()) {
			return NONE;
		}

		return (hiddenSurfacesScratchesLength < HIDDEN_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	@VisibleForTesting
	Recommendation calculateForDents() {
		if (hasNoDents()) {
			return NONE;
		}

		return (this.hiddenSurfacesDentsDepth < HIDDEN_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	@VisibleForTesting
	Recommendation calculateForMinorDamage() {
		return hasMinorDamage()
				? RESALE
				: NONE;
	}

	@VisibleForTesting
	Recommendation calculateForMajorDamage() {
		return hasMajorDamage()
				? OUTLET
				: NONE;
	}
}