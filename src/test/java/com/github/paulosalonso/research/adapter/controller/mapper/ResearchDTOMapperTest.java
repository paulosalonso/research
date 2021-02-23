package com.github.paulosalonso.research.adapter.controller.mapper;

import com.github.paulosalonso.research.adapter.controller.dto.ResearchCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchInputDTO;
import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.domain.Research;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class ResearchDTOMapperTest {

    private static final String TENANT = "tenant";

    @InjectMocks
    private ResearchDTOMapper researchDTOMapper;

    @Mock
    private QuestionDTOMapper questionDTOMapper;

    @Test
    public void givenAnResearchWhenMapWithoutQuestionsThenReturnDTO() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        var dto = researchDTOMapper.toDTO(research, false);

        assertThat(dto.getId()).isEqualTo(research.getId());
        assertThat(dto.getTitle()).isEqualTo(research.getTitle());
        assertThat(dto.getDescription()).isEqualTo(research.getDescription());
        assertThat(dto.getStartsOn()).isEqualTo(research.getStartsOn());
        assertThat(dto.getEndsOn()).isEqualTo(research.getEndsOn());

        verifyNoInteractions(questionDTOMapper);
    }

    @Test
    public void givenAnResearchWhenMapWithQuestionsThenReturnDTO() {
        var question = Question.builder()
                .description("description")
                .multiSelect(false)
                .build();
        var research = Research.builder()
                .id(UUID.randomUUID())
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .questions(Set.of(question))
                .build();

        var dto = researchDTOMapper.toDTO(research, true);

        assertThat(dto.getId()).isEqualTo(research.getId());
        assertThat(dto.getTitle()).isEqualTo(research.getTitle());
        assertThat(dto.getDescription()).isEqualTo(research.getDescription());
        assertThat(dto.getStartsOn()).isEqualTo(research.getStartsOn());
        assertThat(dto.getEndsOn()).isEqualTo(research.getEndsOn());
        assertThat(dto.getQuestions()).hasSize(1);

        verify(questionDTOMapper).toDTO(question, true);
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

        var searchCriteria = researchDTOMapper.toDomain(dto, TENANT);

        assertThat(searchCriteria.getTenant()).isEqualTo(TENANT);
        assertThat(searchCriteria.getTitle()).isEqualTo(dto.getTitle());
        assertThat(searchCriteria.getDescription()).isEqualTo(dto.getDescription());
        assertThat(searchCriteria.getStartsOnFrom()).isEqualTo(dto.getStartsOnFrom());
        assertThat(searchCriteria.getStartsOnTo()).isEqualTo(dto.getStartsOnTo());
        assertThat(searchCriteria.getEndsOnFrom()).isEqualTo(dto.getEndsOnFrom());
        assertThat(searchCriteria.getEndsOnTo()).isEqualTo(dto.getEndsOnTo());
    }

    @Test
    public void givenAResearchInputDTOWhenMapThenReturnResearch() {
        var dto = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        var research = researchDTOMapper.toDomain(dto, TENANT);

        assertThat(research.getId()).isNull();
        assertThat(research.getTenant()).isEqualTo(TENANT);
        assertThat(research.getTitle()).isEqualTo(dto.getTitle());
        assertThat(research.getDescription()).isEqualTo(dto.getDescription());
        assertThat(research.getStartsOn()).isEqualTo(dto.getStartsOn());
        assertThat(research.getEndsOn()).isEqualTo(dto.getEndsOn());
    }
}
