package org.personal.washingmachine.entity.embedded;

import org.personal.washingmachine.enums.Recommendation;

public interface Damage {
	boolean isApplicable();

	/**
	 * <p> <b>In the context</b> of improving readability, </p>
	 * <p> <b>facing</b> the concern that developers might overlook the exclamation mark (!) in conditions like <code>!isApplicable()</code>,</p>
	 * <p> <b>we decided</b> to introduce this method </p>
	 * <p> <b>to achieve</b> improved clarity in condition checks, </p>
	 * <p> <b>accepting</b> that this introduces slightly more code. </p>
	 */
	default boolean isNotApplicable() {
		return !isApplicable();
	}
	Recommendation calculate();
}