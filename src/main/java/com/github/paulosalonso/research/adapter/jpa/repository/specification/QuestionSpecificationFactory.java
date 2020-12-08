package com.github.paulosalonso.research.adapter.jpa.repository.specification;

import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.domain.QuestionCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
public class QuestionSpecificationFactory {

    public Specification<QuestionEntity> findByQuestionCriteria(QuestionCriteria questionCriteria) {
        List<Specification<QuestionEntity>> specifications = new ArrayList<>();

        ofNullable(questionCriteria.getDescription())
                .ifPresent(description -> specifications.add(findByDescriptionLike(description)));

        ofNullable(questionCriteria.getMultiSelect())
                .ifPresent(multiSelect -> specifications.add(findByMultiSelect(multiSelect)));

        return specifications.stream().reduce(identity(), Specification::and);
    }

    private Specification<QuestionEntity> identity() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }

    public Specification<QuestionEntity> findById(String id) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(QuestionEntity.Fields.id), id);
    }

    public Specification<QuestionEntity> findByResearchId(String researchId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(QuestionEntity.Fields.research), researchId);
    }

    public Specification<QuestionEntity> findByDescriptionLike(String description) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get(QuestionEntity.Fields.description), "%" + description + "%");
    }

    public Specification<QuestionEntity> findByMultiSelect(Boolean multiSelect) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(QuestionEntity.Fields.multiSelect), multiSelect);
    }
}
