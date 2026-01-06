package org.personal.solarpanel.config;

import org.personal.shared.time.ClockHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Do NOT add this to the application class.
 *
 * Because {@code @EnableJpaAuditing} requires JPA infrastructure beans
 * which are not loaded in {@code @WebMvcTest}, causing MVC slice tests to fail.
 */
@Configuration
/**
 * Enables JPA auditing.
 *
 * Automatically populates fields annotated with @CreatedDate, @LastModifiedDate, @CreatedBy, @LastModifiedBy.
 * All entities are annotated with @AuditingEntityListener (see orm.xml).
 */
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class AuditConfig {

	/**
	 * Provides a DateTimeProvider for Spring Data JPA auditing.
	 *
	 * <p>Purpose:
	 * - Makes @CreatedDate and @LastModifiedDate testable and deterministic.
	 * - Uses system clock in production; tests can override via {@link ClockHolder} methods.
	 */
	@Bean
	public DateTimeProvider auditingDateTimeProvider() {
		return () -> Optional.of(LocalDateTime.now(ClockHolder.getClock()));
	}
}
