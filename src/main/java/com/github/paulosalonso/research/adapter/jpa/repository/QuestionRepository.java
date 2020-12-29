package com.github.paulosalonso.research.adapter.jpa.repository;

import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuestionRepository extends
        JpaRepository<QuestionEntity, String>, JpaSpecificationExecutor<QuestionEntity> {

    @Query("SELECT MAX(o.sequence) FROM Option o WHERE o.question.id = :questionId")
    Optional<Integer> findLastOptionSequence(String questionId);
}
