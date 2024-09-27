package org.personal.washingmachine.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class VisibleSurfaceDamage {

	@Column(name = "visible_surfaces_scratches_length")
	private double visibleSurfacesScratchesLength;

	@Column(name = "visible_surfaces_dents_depth")
	private double visibleSurfacesDentsDepth;

	@Column(name = "visible_surfaces_minor_damage")
	private String visibleSurfacesMinorDamage;

	@Column(name = "visible_surfaces_major_damage")
	private String visibleSurfacesMajorDamage;

	public boolean isApplicable() {
		return hasScratches() ||
				hasDents() ||
				hasMinorDamage() ||
				hasMajorDamage();
	}

	public boolean hasScratches() {
		return visibleSurfacesScratchesLength > 0;
	}

	public boolean hasDents() {
		return visibleSurfacesDentsDepth > 0;
	}

	public boolean hasMinorDamage() {
		return isNotBlank(visibleSurfacesMinorDamage);
	}

	public boolean hasMajorDamage() {
		return isNotBlank(visibleSurfacesMajorDamage);
	}
}