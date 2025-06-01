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
@With
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class VisibleSurfaceDamage implements Damage {
	private static final int VISIBLE_SURFACES_THRESHOLD = 5;

	@Column(name = "visible_surfaces_scratches_length")
	private double visibleSurfacesScratchesLength;

	@Column(name = "visible_surfaces_dents_depth")
	private double visibleSurfacesDentsDepth;

	@Column(name = "visible_surfaces_minor_damage")
	private String visibleSurfacesMinorDamage;

	@Column(name = "visible_surfaces_major_damage")
	private String visibleSurfacesMajorDamage;

	public boolean isNotApplicable() {
		return !isApplicable();
	}

	public boolean hasScratches() {
		return visibleSurfacesScratchesLength > 0;
	}

	public boolean hasNoScratches() {
		return !hasScratches();
	}

	public boolean hasDents() {
		return visibleSurfacesDentsDepth > 0;
	}

	public boolean hasNoDents() {
		return !hasDents();
	}

	public boolean hasMinorDamage() {
		return isNotBlank(visibleSurfacesMinorDamage);
	}

	public boolean hasMajorDamage() {
		return isNotBlank(visibleSurfacesMajorDamage);
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

		return (this.visibleSurfacesScratchesLength < VISIBLE_SURFACES_THRESHOLD)
				? RESALE
				: OUTLET;
	}

	@VisibleForTesting
	Recommendation calculateForDents() {
		if (hasNoDents()) {
			return NONE;
		}

		return (this.visibleSurfacesDentsDepth < VISIBLE_SURFACES_THRESHOLD)
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