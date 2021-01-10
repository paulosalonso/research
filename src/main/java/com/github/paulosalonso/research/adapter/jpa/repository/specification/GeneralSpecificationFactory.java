package com.github.paulosalonso.research.adapter.jpa.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class GeneralSpecificationFactory {
    private GeneralSpecificationFactory(){}

    public static Specification findWithoutFilter() {
        return (root, criteriaQuery, criteriaBuilder) -> null;
    }

    public static <T> Specification<T> orderByAsc(String... fields) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.orderBy(Stream.of(fields)
                    .map(root::get)
                    .map(criteriaBuilder::asc)
                    .collect(toList()));

            return null;
        };
    }
}
