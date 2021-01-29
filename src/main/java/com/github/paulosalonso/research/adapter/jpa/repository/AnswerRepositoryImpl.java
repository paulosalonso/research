package com.github.paulosalonso.research.adapter.jpa.repository;

import com.github.paulosalonso.research.adapter.jpa.model.AnswerEntity;
import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.ResearchSummaryModel;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.AnswerSpecificationFactory;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class AnswerRepositoryImpl implements AnswerRepositoryCustom {

    private final EntityManager entityManager;
    private final AnswerSpecificationFactory answerSpecificationFactory;

    @Override
    public List<ResearchSummaryModel> search(AnswerCriteria answerCriteria) {

        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(ResearchSummaryModel.class);
        var root = criteriaQuery.from(AnswerEntity.class);

        var selection = criteriaBuilder.construct(ResearchSummaryModel.class,
                root.get(AnswerEntity.Fields.question),
                root.get(AnswerEntity.Fields.option),
                criteriaBuilder.count(root));

        var predicate = answerSpecificationFactory
                .findByAnswerCriteria(answerCriteria).toPredicate(root, criteriaQuery, criteriaBuilder);

        criteriaQuery
                .select(selection)
                .where(predicate)
                .groupBy(root.get(AnswerEntity.Fields.question).get(QuestionEntity.Fields.id),
                        root.get(AnswerEntity.Fields.option).get(OptionEntity.Fields.id));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
