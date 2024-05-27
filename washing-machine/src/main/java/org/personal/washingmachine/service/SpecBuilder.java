package org.personal.washingmachine.service;

import org.apache.commons.lang3.StringUtils;
import org.personal.washingmachine.exception.CustomException;
import org.personal.washingmachine.exception.ErrorCode;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SpecBuilder<T> {

    public Specification<T> addStringLikeCondition(String attribute, String value) {
        return (root, query, cb) ->
                (StringUtils.isNotBlank(value))
                        ? cb.like(cb.lower(root.get(attribute)), "%" + value.toLowerCase().trim() + "%")
                        : cb.conjunction();
    }

    public Specification<T> addIntegerEqualCondition(String attribute, Integer value) {
        return (root, query, cb) ->
                (value != null)
                        ? cb.equal(root.get(attribute), value)
                        : cb.conjunction();
    }

    public Specification<T> addTimestampEqualCondition(String attribute, String value) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (StringUtils.isBlank(value)) {
                return predicate;
            }

            try {
                LocalDate localDate = LocalDate.parse(value.trim());
                predicate = cb.equal(root.get(attribute).as(LocalDate.class), localDate);
            } catch (DateTimeParseException e) {
                throw new CustomException(ErrorCode.E_1007, "Invalid date provided");
            }

            return predicate;
        };
    }
}

