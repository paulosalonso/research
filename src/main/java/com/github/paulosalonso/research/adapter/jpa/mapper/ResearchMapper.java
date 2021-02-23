package com.github.paulosalonso.research.adapter.jpa.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.domain.Research;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Component
public class ResearchMapper {

    private final QuestionMapper questionMapper;

    public ResearchEntity copy(Research from, ResearchEntity to) {
        to.setTitle(from.getTitle());
        to.setDescription(from.getDescription());
        to.setStartsOn(from.getStartsOn());
        to.setEndsOn(from.getEndsOn());

        return to;
    }

    public Research toDomain(ResearchEntity researchEntity, boolean fillQuestions) {
        UUID id = researchEntity.getId() != null ? UUID.fromString(researchEntity.getId()) : null;

        var builder = Research.builder()
                .id(id)
                .tenant(researchEntity.getTenant())
                .title(researchEntity.getTitle())
                .description(researchEntity.getDescription())
                .startsOn(researchEntity.getStartsOn())
                .endsOn(researchEntity.getEndsOn());

        if (fillQuestions) {
            builder.questions(researchEntity.getQuestions().stream()
                    .map(question -> questionMapper.toDomain(question, false))
                    .collect(toSet()));
        }

        return builder.build();
    }

    public ResearchEntity toEntity(Research research) {
        String id = research.getId() != null ? research.getId().toString() : null;

        return ResearchEntity.builder()
                .id(id)
                .tenant(research.getTenant())
                .title(research.getTitle())
                .description(research.getDescription())
                .startsOn(research.getStartsOn())
                .endsOn(research.getEndsOn())
                .build();
    }
}
