package com.github.paulosalonso.research.application.mapper;

import com.github.paulosalonso.research.application.dto.ResearchCriteriaDTO;
import com.github.paulosalonso.research.application.dto.ResearchDTO;
import com.github.paulosalonso.research.application.dto.ResearchInputDTO;
import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.domain.ResearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class ResearchDTOMapper {

    public ResearchDTO toDTO(Research research) {
        return ResearchDTO.builder()
                .id(research.getId().toString())
                .title(research.getTitle())
                .description(research.getDescription())
                .startsOn(research.getStartsOn())
                .endsOn(research.getEndsOn())
                .build();
    }

    public ResearchCriteria toDomain(ResearchCriteriaDTO dto) {
        return ResearchCriteria.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startsOnFrom(dto.getStartsOnFrom())
                .startsOnTo(dto.getStartsOnTo())
                .endsOnFrom(dto.getEndsOnFrom())
                .endsOnTo(dto.getEndsOnTo())
                .build();
    }

    public Research toDomain(ResearchInputDTO dto) {
        return Research.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startsOn(dto.getStartsOn())
                .endsOn(dto.getEndsOn())
                .build();
    }
}
