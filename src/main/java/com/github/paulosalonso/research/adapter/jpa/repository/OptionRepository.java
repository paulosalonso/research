package com.github.paulosalonso.research.adapter.jpa.repository;

import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OptionRepository extends
        JpaRepository<OptionEntity, String>, JpaSpecificationExecutor<OptionEntity> {}
