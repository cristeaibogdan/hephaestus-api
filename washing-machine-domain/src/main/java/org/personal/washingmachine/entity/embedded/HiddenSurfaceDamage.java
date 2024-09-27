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
public class HiddenSurfaceDamage {

	@Column(name = "hidden_surfaces_scratches_length")
	private double hiddenSurfacesScratchesLength;

	@Column(name = "hidden_surfaces_dents_depth")
	private double hiddenSurfacesDentsDepth;

	@Column(name = "hidden_surfaces_minor_damage")
	private String hiddenSurfacesMinorDamage;

	@Column(name = "hidden_surfaces_major_damage")
	private String hiddenSurfacesMajorDamage;

	public boolean isApplicable() {
		return hasScratches() ||
				hasDents() ||
				hasMinorDamage() ||
				hasMajorDamage();
	}

	public boolean hasScratches() {
		return hiddenSurfacesScratchesLength > 0;
	}

	public boolean hasDents() {
		return hiddenSurfacesDentsDepth > 0;
	}

	public boolean hasMinorDamage() {
		return isNotBlank(hiddenSurfacesMinorDamage);
	}

	public boolean hasMajorDamage() {
		return isNotBlank(hiddenSurfacesMajorDamage);
	}
}