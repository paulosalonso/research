package com.github.paulosalonso.research.adapter.jpa.repository.specification;

import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.domain.ResearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.JoinType;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.paulosalonso.research.adapter.jpa.repository.specification.GeneralSpecificationFactory.findWithoutFilter;
import static java.util.Optional.ofNullable;

@Component
public class ResearchSpecificationFactory {

    public Specification<ResearchEntity> findByResearchCriteria(ResearchCriteria researchCriteria) {
        List<Specification<ResearchEntity>> specifications = new ArrayList<>();

        ofNullable(researchCriteria.getTitle())
                .ifPresent(title -> specifications.add(findByTitleLike(researchCriteria.getTitle())));

        ofNullable(researchCriteria.getDescription())
                .ifPresent(description -> specifications.add(findByDescriptionLike(description)));

        ofNullable(researchCriteria.getStartsOnFrom())
                .ifPresent(startsOn -> specifications.add(findByStartsOnFrom(startsOn)));

        ofNullable(researchCriteria.getStartsOnTo())
                .ifPresent(startsOn -> specifications.add(findByStartsOnTo(startsOn)));

        ofNullable(researchCriteria.getEndsOnFrom())
                .ifPresent(endsOn -> specifications.add(findByEndsOnFrom(endsOn)));

        ofNullable(researchCriteria.getEndsOnTo())
                .ifPresent(endsOn -> specifications.add(findByEndsOnTo(endsOn)));

        return specifications.stream().reduce(findWithoutFilter(), Specification::and);
    }

    public Specification<ResearchEntity> findById(String id) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(ResearchEntity.Fields.id), id);
    }

    public Specification<ResearchEntity> findByTitleLike(String title) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get(ResearchEntity.Fields.title), "%" + title + "%");
    }

    public Specification<ResearchEntity> findByDescriptionLike(String description) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get(ResearchEntity.Fields.description), "%" + description + "%");
    }

    public Specification<ResearchEntity> findByStartsOnFrom(OffsetDateTime startsOnFrom) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get(ResearchEntity.Fields.startsOn), startsOnFrom);
    }

    public Specification<ResearchEntity> findByStartsOnTo(OffsetDateTime startsOnTo) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(ResearchEntity.Fields.startsOn), startsOnTo);
    }

    public Specification<ResearchEntity> findByEndsOnFrom(OffsetDateTime endsOnFrom) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get(ResearchEntity.Fields.endsOn), endsOnFrom);
    }

    public Specification<ResearchEntity> findByEndsOnTo(OffsetDateTime endsOnTo) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get(ResearchEntity.Fields.endsOn), endsOnTo);
    }

    public Specification<ResearchEntity> findFetchingQuestions() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            root.fetch(ResearchEntity.Fields.questions, JoinType.LEFT);
            return null;
        };
    }
}
