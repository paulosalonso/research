package com.github.paulosalonso.research.adapter.jpa.repository;

import com.github.paulosalonso.research.adapter.jpa.model.ResearchSummaryModel;
import com.github.paulosalonso.research.domain.AnswerCriteria;

import java.util.List;

public interface AnswerRepositoryCustom {

    List<ResearchSummaryModel> search(AnswerCriteria criteria);
}
