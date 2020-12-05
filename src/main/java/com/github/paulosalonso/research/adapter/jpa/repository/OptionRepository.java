package com.github.paulosalonso.research.adapter.jpa.repository;

import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<OptionEntity, String> {

    List<OptionEntity> findByQuestionId(String questionId);
    List<OptionEntity> findByQuestionIdAndId(String questionId, String optionId);

}
