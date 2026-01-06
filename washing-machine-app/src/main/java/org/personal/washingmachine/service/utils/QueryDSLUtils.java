package org.personal.washingmachine.service.utils;

import com.querydsl.core.types.dsl.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Older implementation which was deprecated in favor of using plain old ifs to construct a predicate.
 * Usage:
 * <pre>{@code
 * 	private BooleanBuilder buildSearchPredicate(SearchWashingMachineRequest request) {
 * 		return new BooleanBuilder()
 * 				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.identificationMode, request.identificationMode()))
 * 				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.manufacturer, request.manufacturer()))
 *
 * 				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.model, request.model()))
 * 				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.type, request.type()))
 * 				.and(QueryDSLUtils.addStringLikeCondition(washingMachine.serialNumber, request.serialNumber()))
 *
 * 				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.returnType, request.returnType()))
 * 				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.damageType, request.damageType()))
 * 				.and(QueryDSLUtils.addEnumEqualCondition(washingMachine.recommendation, request.recommendation()))
 *
 * 				.and(QueryDSLUtils.addTimestampEqualCondition(washingMachine.createdAt, parseToLocalDate(request.createdAt())));
 *        }
 * }</pre>
 */
@Deprecated
@NoArgsConstructor(access = AccessLevel.NONE)
public final class QueryDSLUtils {

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

	public static BooleanExpression addIntegerEqualCondition(NumberPath<Integer> attribute, Integer value) {
		return (value != null)
				? attribute.eq(value)
				: null;
	}

	public static BooleanExpression addDateEqualCondition(DatePath<LocalDate> attribute, LocalDate value) {
		return attribute.eq(value);
	}

	// TODO: Add an if to make sure the startDate is before the endDate
	public static BooleanExpression addDateBetweenCondition(DatePath<LocalDate> attribute, LocalDate startDate, LocalDate endDate) {
		return attribute.between(startDate, endDate);
	}

	public static BooleanExpression addTimestampEqualCondition(DateTimePath<LocalDateTime> attribute, LocalDate value) {
		return attribute.year().eq(value.getYear())
				.and(attribute.month().eq(value.getMonthValue()))
				.and(attribute.dayOfMonth().eq(value.getDayOfMonth()));
	}
}
