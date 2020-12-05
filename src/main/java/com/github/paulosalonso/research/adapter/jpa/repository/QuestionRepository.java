package com.github.paulosalonso.research.adapter.jpa.repository;

import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity, String> {

    List<Question> findByResearchId(String researchId);
    List<Question> findByResearchIdAndId(String researchId, String questionId);

}
