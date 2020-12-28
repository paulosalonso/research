package com.github.paulosalonso.research.adapter.jpa.repository;

import com.github.paulosalonso.research.adapter.jpa.model.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnswerRepository extends
        JpaRepository<AnswerEntity, Long>, JpaSpecificationExecutor<AnswerEntity>, AnswerRepositoryCustom {}
