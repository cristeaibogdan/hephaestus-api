package org.personal.washingmachine.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class HiddenSurfaceDamage {

	@Column(name = "hidden_surfaces_scratches_length")
	private double hiddenSurfacesScratchesLength;


	@Column(name = "hidden_surfaces_has_dents")
	private boolean hiddenSurfacesHasDents;

	@Column(name = "hidden_surfaces_dents_depth")
	private double hiddenSurfacesDentsDepth;


	@Column(name = "hidden_surfaces_has_minor_damage")
	private boolean hiddenSurfacesHasMinorDamage;

	@Column(name = "hidden_surfaces_minor_damage")
	private String hiddenSurfacesMinorDamage;


	@Column(name = "hidden_surfaces_has_major_damage")
	private boolean hiddenSurfacesHasMajorDamage;

	@Column(name = "hidden_surfaces_major_damage")
	private String hiddenSurfacesMajorDamage;

	public boolean isApplicable() {
		return hasScratches() ||
				hiddenSurfacesHasDents ||
				hiddenSurfacesHasMinorDamage ||
				hiddenSurfacesHasMajorDamage;
	}

	public boolean hasScratches() { // TODO: Min(0) and Max(10) can be set into DTO validation
		return hiddenSurfacesScratchesLength > 0;
	}

	public static HiddenSurfaceDamageBuilder builder() { //TODO: Replace with lombok @Builder
		return new HiddenSurfaceDamageBuilder();
	}

	public static class HiddenSurfaceDamageBuilder {
		private double hiddenSurfacesScratchesLength;

		private boolean hiddenSurfacesHasDents;
		private double hiddenSurfacesDentsDepth;

		private boolean hiddenSurfacesHasMinorDamage;
		private String hiddenSurfacesMinorDamage;

		private boolean hiddenSurfacesHasMajorDamage;
		private String hiddenSurfacesMajorDamage;

		public HiddenSurfaceDamage build() {
			return new HiddenSurfaceDamage(
					this.hiddenSurfacesScratchesLength,
					this.hiddenSurfacesHasDents,
					this.hiddenSurfacesDentsDepth,
					this.hiddenSurfacesHasMinorDamage,
					this.hiddenSurfacesMinorDamage,
					this.hiddenSurfacesHasMajorDamage,
					this.hiddenSurfacesMajorDamage
			);
		}

        public HiddenSurfaceDamageBuilder hiddenSurfacesScratchesLength (double hiddenSurfacesScratchesLength) {
            this.hiddenSurfacesScratchesLength = hiddenSurfacesScratchesLength;
            return this;
        }

        public HiddenSurfaceDamageBuilder hiddenSurfacesHasDents (boolean hiddenSurfacesHasDents) {
            this.hiddenSurfacesHasDents = hiddenSurfacesHasDents;
            return this;
        }

        public HiddenSurfaceDamageBuilder hiddenSurfacesDentsDepth (double hiddenSurfacesDentsDepth) {
            this.hiddenSurfacesDentsDepth = hiddenSurfacesDentsDepth;
            return this;
        }

        public HiddenSurfaceDamageBuilder hiddenSurfacesHasMinorDamage (boolean hiddenSurfacesHasMinorDamage) {
            this.hiddenSurfacesHasMinorDamage = hiddenSurfacesHasMinorDamage;
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
