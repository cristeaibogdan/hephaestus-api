package org.personal.solarpanel.service.utils;

import com.querydsl.core.types.dsl.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class QueryDSLUtils { // TODO: Duplicated class. Should be extracted.

	public static BooleanExpression addStringLikeCondition(StringPath attribute, String value) {
		return StringUtils.isNotBlank(value)
				? attribute.containsIgnoreCase(value.trim())
				: null;
	}

	public static <T extends Enum<T>> BooleanExpression addEnumEqualCondition(EnumPath<T> attribute, T value) {
		return (value != null)
				? attribute.eq(value)
				: null;
	}
	public static BooleanExpression addTimestampEqualCondition(DateTimePath<LocalDateTime> attribute, Optional<LocalDate> value) {
		return value.map(
						v -> attribute.year().eq(v.getYear())
								.and(attribute.month().eq(v.getMonthValue()))
								.and(attribute.dayOfMonth().eq(v.getDayOfMonth()))
				)
				.orElse(null);
	}
}
