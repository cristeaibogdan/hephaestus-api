package org.personal.solarpanel.time;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Clock;

/**
 * Provides a controllable Clock for time-dependent operations.
 * In production, uses system time. In tests, can be set to fixed times.
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public class ClockHolder {
	/**
	 * Uses ThreadLocal for thread safety. Each thread has its own clock.
	 */
	private static final ThreadLocal<Clock> clock = ThreadLocal.withInitial(Clock::systemDefaultZone);

	public static Clock getClock() {
		return clock.get();
	}

	/**
	 * Sets a custom clock. Use only in tests.
	 */
	public static void setClock(Clock newClock) {
		clock.set(newClock);
	}

	/**
	 * Resets to system default time. Always call in @AfterEach or just after saving an entity to a DB.
	 */
	public static void reset() {
		clock.remove();
	}
}
