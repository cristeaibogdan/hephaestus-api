package org.personal.washingmachine.service.utils;

import com.querydsl.core.types.dsl.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.enums.ReturnType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class QueryDSLUtils {

    public static BooleanExpression addStringLikeCondition(StringPath attribute, String value) {
        return StringUtils.isNotBlank(value)
                ? attribute.containsIgnoreCase(value.trim())
                : null;
    }

    public static BooleanExpression addEnumEqualCondition(EnumPath<ReturnType> attribute, ReturnType value) {
        return (value != null)
                ? attribute.eq(value)
                : null;
    }

    public static BooleanExpression addIntegerEqualCondition(NumberPath<Integer> attribute, Integer value) {
        return (value != null)
                ? attribute.eq(value)
                : null;
    }

    public static BooleanExpression addDateEqualCondition(DatePath<LocalDate> attribute, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            LocalDate localDate = LocalDate.parse(value.trim());
            return attribute.eq(localDate);
        } catch (DateTimeParseException e) {
            throw new CustomException("Invalid date provided", ErrorCode.INVALID_DATE, e);
        }
    }

    public static BooleanExpression addDateBetweenCondition(DatePath<LocalDate> attribute, String startDate, String endDate) {
        if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
            return null;
        }

        try {
            LocalDate convertedStartDate = LocalDate.parse(startDate.trim());
            LocalDate convertedEndDate = LocalDate.parse(endDate.trim());
            return attribute.between(convertedStartDate, convertedEndDate);
        } catch (DateTimeParseException e) {
            throw new CustomException("Invalid date provided", ErrorCode.INVALID_DATE, e);
        }
    }

    public static BooleanExpression addTimestampEqualCondition(DateTimePath<LocalDateTime> attribute, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            LocalDate localDate = LocalDate.parse(value.trim());

            return attribute.year().eq(localDate.getYear())
                    .and(attribute.month().eq(localDate.getMonthValue()))
                    .and(attribute.dayOfMonth().eq(localDate.getDayOfMonth()));

        } catch (DateTimeParseException e) {
            throw new CustomException("Invalid date provided", ErrorCode.INVALID_DATE, e);
        }
    }

}
