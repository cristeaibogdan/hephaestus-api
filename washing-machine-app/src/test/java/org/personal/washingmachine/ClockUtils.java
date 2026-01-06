package org.personal.washingmachine;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Utility methods for creating fixed clocks.
 *
 * <p>Purpose:
 * - Simplifies creation of fixed Clock instances for tests.
 * - Ensures deterministic behavior when testing time-dependent logic.
 */
public class ClockUtils {
  public static Clock fixedClock(LocalDate date) {
    Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
    return Clock.fixed(instant, ZoneId.systemDefault());
  }
}
