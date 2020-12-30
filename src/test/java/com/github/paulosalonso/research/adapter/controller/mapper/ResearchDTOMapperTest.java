package com.github.paulosalonso.research.adapter.controller.mapper;

import com.github.paulosalonso.research.adapter.controller.dto.ResearchCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchInputDTO;
import com.github.paulosalonso.research.domain.Research;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ResearchDTOMapperTest {

    private ResearchDTOMapper mapper = new ResearchDTOMapper();

    @Test
    public void givenAnResearchWhenMapThenReturnDTO() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        var dto = mapper.toDTO(research);

        assertThat(dto.getId()).isEqualTo(research.getId().toString());
        assertThat(dto.getTitle()).isEqualTo(research.getTitle());
        assertThat(dto.getDescription()).isEqualTo(research.getDescription());
        assertThat(dto.getStartsOn()).isEqualTo(research.getStartsOn());
        assertThat(dto.getEndsOn()).isEqualTo(research.getEndsOn());
    }

    @Test
    public void givenAnResearchCriteriaDTOWhenMapThenReturnResearchCriteria() {
        var dto = ResearchCriteriaDTO.builder()
                .title("title")
                .description("description")
                .startsOnFrom(OffsetDateTime.now())
                .startsOnTo(OffsetDateTime.now().plusDays(5))
                .endsOnFrom(OffsetDateTime.now().plusMonths(1))
                .endsOnTo(OffsetDateTime.now().plusMonths(1).plusDays(5))
                .build();

        var searchCriteria = mapper.toDomain(dto);

        assertThat(searchCriteria.getTitle()).isEqualTo(dto.getTitle());
        assertThat(searchCriteria.getDescription()).isEqualTo(dto.getDescription());
        assertThat(searchCriteria.getStartsOnFrom()).isEqualTo(dto.getStartsOnFrom());
        assertThat(searchCriteria.getStartsOnTo()).isEqualTo(dto.getStartsOnTo());
        assertThat(searchCriteria.getEndsOnFrom()).isEqualTo(dto.getEndsOnFrom());
        assertThat(searchCriteria.getEndsOnTo()).isEqualTo(dto.getEndsOnTo());
    }

    @Test
    public void givenAResearchInputDTOWhenMapThenReturnResearch() {
        var research = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        var dto = mapper.toDomain(research);

        assertThat(dto.getId()).isNull();
        assertThat(dto.getTitle()).isEqualTo(research.getTitle());
        assertThat(dto.getDescription()).isEqualTo(research.getDescription());
        assertThat(dto.getStartsOn()).isEqualTo(research.getStartsOn());
        assertThat(dto.getEndsOn()).isEqualTo(research.getEndsOn());
    }
}
