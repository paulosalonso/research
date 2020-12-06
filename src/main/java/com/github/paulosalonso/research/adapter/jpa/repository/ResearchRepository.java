package com.github.paulosalonso.research.adapter.jpa.repository;

import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResearchRepository extends
        JpaRepository<ResearchEntity, String>, JpaSpecificationExecutor<ResearchEntity> {}
