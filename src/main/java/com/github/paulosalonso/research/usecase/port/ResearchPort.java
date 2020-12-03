package com.github.paulosalonso.research.usecase.port;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.domain.ResearchCriteria;

import java.util.List;
import java.util.UUID;

public interface ResearchPort {

    Research create(Research research);
    Research read(UUID id);
    List<Research> search(ResearchCriteria criteria);
    Research update(Research research);
    void delete(UUID id);
}
