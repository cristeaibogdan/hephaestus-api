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

	/**
	 * <p> <b>In the context</b> of improving readability, </p>
	 * <p> <b>facing</b> the concern that developers might overlook the exclamation mark (!) in conditions like <code>!isApplicable()</code>,</p>
	 * <p> <b>we decided</b> to introduce this method </p>
	 * <p> <b>to achieve</b> improved clarity in condition checks, </p>
	 * <p> <b>accepting</b> that this introduces slightly more code. </p>
	 */
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
}