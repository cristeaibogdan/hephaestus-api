package org.personal.washingmachine.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.*;

@Getter
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


	@Column(name = "hidden_surfaces_has_major_damage")
	private boolean hiddenSurfacesHasMajorDamage;

	@Column(name = "hidden_surfaces_major_damage")
	private String hiddenSurfacesMajorDamage;

	public boolean isApplicable() {
		return hasScratches() ||
				hasDents() ||
				hasMinorDamage() ||
				hiddenSurfacesHasMajorDamage;
	}

	public boolean hasScratches() { // TODO: Min(0) and Max(10) can be set into DTO validation
		return hiddenSurfacesScratchesLength > 0;
	}

	public boolean hasDents() { // TODO: Min(0) and Max(10) can be set into DTO validation
		return hiddenSurfacesDentsDepth > 0;
	}

	public boolean hasMinorDamage() { // TODO: @NotNull can be set into DTO validation
		return isNotBlank(hiddenSurfacesMinorDamage);
	}

	public static HiddenSurfaceDamageBuilder builder() { //TODO: Replace with lombok @Builder
		return new HiddenSurfaceDamageBuilder();
	}

	public static class HiddenSurfaceDamageBuilder {
		private double hiddenSurfacesScratchesLength;
		private double hiddenSurfacesDentsDepth;

		private String hiddenSurfacesMinorDamage;

		private boolean hiddenSurfacesHasMajorDamage;
		private String hiddenSurfacesMajorDamage;

		public HiddenSurfaceDamage build() {
			return new HiddenSurfaceDamage(
					this.hiddenSurfacesScratchesLength,
					this.hiddenSurfacesDentsDepth,
					this.hiddenSurfacesMinorDamage,
					this.hiddenSurfacesHasMajorDamage,
					this.hiddenSurfacesMajorDamage
			);
		}

        public HiddenSurfaceDamageBuilder hiddenSurfacesScratchesLength (double hiddenSurfacesScratchesLength) {
            this.hiddenSurfacesScratchesLength = hiddenSurfacesScratchesLength;
            return this;
        }

        public HiddenSurfaceDamageBuilder hiddenSurfacesDentsDepth (double hiddenSurfacesDentsDepth) {
            this.hiddenSurfacesDentsDepth = hiddenSurfacesDentsDepth;
            return this;
        }

        public HiddenSurfaceDamageBuilder hiddenSurfacesMinorDamage (String hiddenSurfacesMinorDamage) {
            this.hiddenSurfacesMinorDamage = hiddenSurfacesMinorDamage;
            return this;
        }

        public HiddenSurfaceDamageBuilder hiddenSurfacesHasMajorDamage (boolean hiddenSurfacesHasMajorDamage) {
            this.hiddenSurfacesHasMajorDamage = hiddenSurfacesHasMajorDamage;
            return this;
        }

        public HiddenSurfaceDamageBuilder hiddenSurfacesMajorDamage (String hiddenSurfacesMajorDamage) {
            this.hiddenSurfacesMajorDamage = hiddenSurfacesMajorDamage;
            return this;
        }
	}
}
