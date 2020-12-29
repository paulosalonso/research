package com.github.paulosalonso.research.adapter.jpa.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public final class GeneralSpecificationFactory {
    private GeneralSpecificationFactory(){}

    static Specification findWithoutFilter() {
        return (root, criteriaQuery, criteriaBuilder) -> null;
    }

    public static <T> Specification<T> orderByAsc(String... fields) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            var preExistentOrders = ofNullable(criteriaQuery.getOrderList())
                    .orElse(new ArrayList<>());

            var orders = new ArrayList<>(preExistentOrders);

            Stream.of(fields)
                    .map(field -> criteriaBuilder.asc(root.get(field)))
                    .peek(orders::add);

            criteriaQuery.orderBy(orders);

            return null;
        };
    }

    public static <T> Specification<T> orderByDesc(String... fields) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            var preExistentOrders = ofNullable(criteriaQuery.getOrderList())
                    .orElse(new ArrayList<>());

            var orders = new ArrayList<>(preExistentOrders);

            Stream.of(fields)
                    .map(field -> criteriaBuilder.desc(root.get(field)))
                    .peek(orders::add);

            criteriaQuery.orderBy(orders);

            return null;
        };
    }
}
