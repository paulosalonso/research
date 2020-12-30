package com.github.paulosalonso.research.adapter.jpa.repository;

import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResearchRepository extends
        JpaRepository<ResearchEntity, String>, JpaSpecificationExecutor<ResearchEntity> {

    @Query("SELECT MAX(q.sequence) FROM Question q WHERE q.research.id = :researchId")
    Optional<Integer> findLastQuestionSequence(String researchId);
}
