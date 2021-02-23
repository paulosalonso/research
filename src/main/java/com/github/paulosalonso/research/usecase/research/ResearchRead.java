package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.domain.ResearchCriteria;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ResearchRead {

    private final ResearchPort researchPort;

    public Research read(UUID id, String tenant, boolean fillQuestions) {
        if (fillQuestions) {
            return researchPort.readFetchingQuestions(id, tenant);
        }

        return researchPort.read(id, tenant);
    }

    public List<Research> search(ResearchCriteria criteria) {
        return researchPort.search(criteria);
    }
}
