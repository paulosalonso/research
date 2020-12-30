package com.github.paulosalonso.research.adapter.jpa.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.domain.Research;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResearchMapper {

    public ResearchEntity copy(Research from, ResearchEntity to) {
        to.setTitle(from.getTitle());
        to.setDescription(from.getDescription());
        to.setStartsOn(from.getStartsOn());
        to.setEndsOn(from.getEndsOn());

        return to;
    }

    public Research toDomain(ResearchEntity researchEntity) {
        UUID id = researchEntity.getId() != null ? UUID.fromString(researchEntity.getId()) : null;

        return Research.builder()
                .id(id)
                .title(researchEntity.getTitle())
                .description(researchEntity.getDescription())
                .startsOn(researchEntity.getStartsOn())
                .endsOn(researchEntity.getEndsOn())
                .build();
    }

    public ResearchEntity toEntity(Research research) {
        String id = research.getId() != null ? research.getId().toString() : null;

        return ResearchEntity.builder()
                .id(id)
                .title(research.getTitle())
                .description(research.getDescription())
                .startsOn(research.getStartsOn())
                .endsOn(research.getEndsOn())
                .build();
    }
}
