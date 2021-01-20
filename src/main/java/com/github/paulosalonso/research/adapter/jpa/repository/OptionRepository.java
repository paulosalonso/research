package com.github.paulosalonso.research.adapter.jpa.repository;

import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends
        JpaRepository<OptionEntity, String>, JpaSpecificationExecutor<OptionEntity> {

    @Query("SELECT o.notify FROM Option o WHERE id = :id")
    Boolean findNotifyById(String id);
}
