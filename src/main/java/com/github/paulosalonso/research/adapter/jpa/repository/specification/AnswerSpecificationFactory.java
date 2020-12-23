package com.github.paulosalonso.research.adapter.jpa.repository.specification;

import com.github.paulosalonso.research.adapter.jpa.model.AnswerEntity;
import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.paulosalonso.research.adapter.jpa.repository.specification.NoFilterSpecificationFactory.findWithoutFilter;
import static java.util.Optional.ofNullable;

@Component
public class AnswerSpecificationFactory {

    public Specification<AnswerEntity> findByAnswerCriteria(AnswerCriteria answerCriteria) {
        List<Specification<AnswerEntity>> specifications = new ArrayList<>();

        ofNullable(answerCriteria.getDateFrom())
                .ifPresent(date -> specifications.add(findByDateFrom(date)));

        ofNullable(answerCriteria.getDateTo())
                .ifPresent(date -> specifications.add(findByDateTo(date)));

        ofNullable(answerCriteria.getResearchId())
                .ifPresent(researchId -> specifications.add(findByResearchId(researchId)));

        ofNullable(answerCriteria.getQuestionId())
                .ifPresent(questionId -> specifications.add(findByQuestionId(questionId)));

        return specifications.stream().reduce(findWithoutFilter(), Specification::and);
    }

    public Specification<AnswerEntity> findByDateFrom(OffsetDateTime date) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get(AnswerEntity.Fields.date), date);
    }

    public Specification<AnswerEntity> findByDateTo(OffsetDateTime date) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(AnswerEntity.Fields.date), date);
    }

    public Specification<AnswerEntity> findByResearchId(UUID researchId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(AnswerEntity.Fields.research).get(ResearchEntity.Fields.id), researchId.toString());
    }

    public Specification<AnswerEntity> findByQuestionId(UUID questionId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(AnswerEntity.Fields.question).get(QuestionEntity.Fields.id), questionId.toString());
    }

}
