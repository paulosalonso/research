package com.github.paulosalonso.research.adapter.jpa.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.domain.Research;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class ResearchMapperTest {

    private static final String TENANT = "tenant";

    @InjectMocks
    private ResearchMapper researchMapper;

    @Mock
    private QuestionMapper questionMapper;

    @Test
    public void givenAResearchWhenMapThenReturnEntity() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .tenant(TENANT)
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now())
                .build();

        var entity = researchMapper.toEntity(research);

        assertThat(entity.getId()).isEqualTo(research.getId().toString());
        assertThat(entity.getTenant()).isEqualTo(research.getTenant());
        assertThat(entity.getTitle()).isEqualTo(research.getTitle());
        assertThat(entity.getDescription()).isEqualTo(research.getDescription());
        assertThat(entity.getStartsOn()).isEqualTo(research.getStartsOn());
        assertThat(entity.getEndsOn()).isEqualTo(research.getEndsOn());
    }

    @Test
    public void givenAResearchWithoutIdWhenMapThenReturnEntity() {
        var research = Research.builder()
                .tenant(TENANT)
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now())
                .build();

        var entity = researchMapper.toEntity(research);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getTenant()).isEqualTo(research.getTenant());
        assertThat(entity.getTitle()).isEqualTo(research.getTitle());
        assertThat(entity.getDescription()).isEqualTo(research.getDescription());
        assertThat(entity.getStartsOn()).isEqualTo(research.getStartsOn());
        assertThat(entity.getEndsOn()).isEqualTo(research.getEndsOn());
    }

    @Test
    public void givenAResearchEntityWhenMapWithoutQuestionsThenReturnDomain() {
        var entity = ResearchEntity.builder()
                .id(UUID.randomUUID().toString())
                .tenant(TENANT)
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now())
                .build();

        var research = researchMapper.toDomain(entity, false);

        assertThat(research.getId()).isEqualTo(UUID.fromString(entity.getId()));
        assertThat(research.getTenant()).isEqualTo(entity.getTenant());
        assertThat(research.getTitle()).isEqualTo(entity.getTitle());
        assertThat(research.getDescription()).isEqualTo(entity.getDescription());
        assertThat(research.getStartsOn()).isEqualTo(entity.getStartsOn());
        assertThat(research.getEndsOn()).isEqualTo(entity.getEndsOn());

        verifyNoInteractions(questionMapper);
    }

    @Test
    public void givenAResearchEntityWhenMapWithQuestionsThenReturnDomain() {
        var question = QuestionEntity.builder().build();
        var entity = ResearchEntity.builder()
                .id(UUID.randomUUID().toString())
                .tenant(TENANT)
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now())
                .questions(List.of(question))
                .build();

        var research = researchMapper.toDomain(entity, true);

        assertThat(research.getId()).isEqualTo(UUID.fromString(entity.getId()));
        assertThat(research.getTenant()).isEqualTo(entity.getTenant());
        assertThat(research.getTitle()).isEqualTo(entity.getTitle());
        assertThat(research.getDescription()).isEqualTo(entity.getDescription());
        assertThat(research.getStartsOn()).isEqualTo(entity.getStartsOn());
        assertThat(research.getEndsOn()).isEqualTo(entity.getEndsOn());
        assertThat(research.getQuestions()).hasSize(1);

        verify(questionMapper).toDomain(question, false);
    }

    @Test
    public void givenAResearchEntityWithoutIdWhenMapThenReturnDomain() {
        var entity = ResearchEntity.builder()
                .tenant(TENANT)
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now())
                .build();

        var research = researchMapper.toDomain(entity, false);

        assertThat(research.getId()).isNull();
        assertThat(research.getTenant()).isEqualTo(entity.getTenant());
        assertThat(research.getTitle()).isEqualTo(entity.getTitle());
        assertThat(research.getDescription()).isEqualTo(entity.getDescription());
        assertThat(research.getStartsOn()).isEqualTo(entity.getStartsOn());
        assertThat(research.getEndsOn()).isEqualTo(entity.getEndsOn());
    }

}
