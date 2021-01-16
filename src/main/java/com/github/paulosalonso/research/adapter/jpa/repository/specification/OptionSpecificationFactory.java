package com.github.paulosalonso.research.adapter.jpa.repository.specification;

import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.domain.OptionCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.github.paulosalonso.research.adapter.jpa.repository.specification.GeneralSpecificationFactory.findWithoutFilter;
import static java.util.Optional.ofNullable;

@Component
public class OptionSpecificationFactory {

    public Specification<OptionEntity> findByOptionCriteria(OptionCriteria OptionCriteria) {
        List<Specification<OptionEntity>> specifications = new ArrayList<>();

        ofNullable(OptionCriteria.getDescription())
                .ifPresent(description -> specifications.add(findByDescriptionLike(description)));

        ofNullable(OptionCriteria.getNotify())
                .ifPresent(notify -> specifications.add(findByNotify(notify)));

        return specifications.stream().reduce(findWithoutFilter(), Specification::and);
    }

    public Specification<OptionEntity> findById(String id) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(OptionEntity.Fields.id), id);
    }

    public Specification<OptionEntity> findByQuestionId(String questionId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(OptionEntity.Fields.question).get(QuestionEntity.Fields.id), questionId);
    }

    public Specification<OptionEntity> findByDescriptionLike(String description) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get(OptionEntity.Fields.description), "%" + description + "%");
    }

    public Specification<OptionEntity> findByNotify(boolean notify) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(OptionEntity.Fields.notify), notify);
    }
}
