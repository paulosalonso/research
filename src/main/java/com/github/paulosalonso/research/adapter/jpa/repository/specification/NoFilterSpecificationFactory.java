package com.github.paulosalonso.research.adapter.jpa.repository.specification;

import org.springframework.data.jpa.domain.Specification;

public final class NoFilterSpecificationFactory {
    private NoFilterSpecificationFactory(){}

    static Specification findWithoutFilter() {
        return (root, criteriaQuery, criteriaBuilder) -> null;
    }
}
