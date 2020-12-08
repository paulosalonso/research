package com.github.paulosalonso.research.adapter.jpa.repository.specification;

import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.domain.OptionCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
public class OptionSpecificationFactory {

    public Specification<OptionEntity> findByOptionCriteria(OptionCriteria OptionCriteria) {
        List<Specification<OptionEntity>> specifications = new ArrayList<>();

        ofNullable(OptionCriteria.getDescription())
                .ifPresent(description -> specifications.add(findByDescriptionLike(description)));

        return specifications.stream().reduce(identity(), Specification::and);
    }

    private Specification<OptionEntity> identity() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }

    public Specification<OptionEntity> findById(String id) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(OptionEntity.Fields.id), id);
    }

    public Specification<OptionEntity> findByQuestionId(String questionId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(OptionEntity.Fields.question), questionId);
    }

    public Specification<OptionEntity> findByDescriptionLike(String description) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get(OptionEntity.Fields.description), "%" + description + "%");
    }
}
