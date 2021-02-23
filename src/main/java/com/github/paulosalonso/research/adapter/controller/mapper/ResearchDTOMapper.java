package com.github.paulosalonso.research.adapter.controller.mapper;

import com.github.paulosalonso.research.adapter.controller.dto.ResearchCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchInputDTO;
import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.domain.ResearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class ResearchDTOMapper {

    private final QuestionDTOMapper questionDTOMapper;

    public ResearchDTO toDTO(Research research, boolean fillQuestions) {
        var builder = ResearchDTO.builder()
                .id(research.getId())
                .title(research.getTitle())
                .description(research.getDescription())
                .startsOn(research.getStartsOn())
                .endsOn(research.getEndsOn());

        if (fillQuestions) {
            builder.questions(research.getQuestions().stream()
                    .map(question -> questionDTOMapper.toDTO(question, true))
                    .collect(toList()));
        }

        return builder.build();
    }

    public ResearchCriteria toDomain(ResearchCriteriaDTO dto, String tenant) {
        return ResearchCriteria.builder()
                .tenant(tenant)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startsOnFrom(dto.getStartsOnFrom())
                .startsOnTo(dto.getStartsOnTo())
                .endsOnFrom(dto.getEndsOnFrom())
                .endsOnTo(dto.getEndsOnTo())
                .build();
    }

    public Research toDomain(ResearchInputDTO dto, String tenant) {
        return Research.builder()
                .tenant(tenant)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startsOn(dto.getStartsOn())
                .endsOn(dto.getEndsOn())
                .build();
    }
}
