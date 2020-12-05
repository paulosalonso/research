package com.github.paulosalonso.research.adapter.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.domain.Research;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ResearchMapperTest {

    private final ResearchMapper mapper = new ResearchMapper();

    @Test
    public void givenAResearchWhenMapThenReturnEntity() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now())
                .build();

        var entity = mapper.toEntity(research);

        assertThat(entity.getId()).isEqualTo(research.getId().toString());
        assertThat(entity.getTitle()).isEqualTo(research.getTitle());
        assertThat(entity.getDescription()).isEqualTo(research.getDescription());
        assertThat(entity.getStartsOn()).isEqualTo(research.getStartsOn());
        assertThat(entity.getEndsOn()).isEqualTo(research.getEndsOn());
    }

    @Test
    public void givenAResearchWithoutIdWhenMapThenReturnEntity() {
        var research = Research.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now())
                .build();

        var entity = mapper.toEntity(research);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getTitle()).isEqualTo(research.getTitle());
        assertThat(entity.getDescription()).isEqualTo(research.getDescription());
        assertThat(entity.getStartsOn()).isEqualTo(research.getStartsOn());
        assertThat(entity.getEndsOn()).isEqualTo(research.getEndsOn());
    }

    @Test
    public void givenAResearchEntityWhenMapThenReturnDomain() {
        var entity = ResearchEntity.builder()
                .id(UUID.randomUUID().toString())
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now())
                .build();

        var research = mapper.toDomain(entity);

        assertThat(research.getId()).isEqualTo(UUID.fromString(entity.getId()));
        assertThat(research.getTitle()).isEqualTo(entity.getTitle());
        assertThat(research.getDescription()).isEqualTo(entity.getDescription());
        assertThat(research.getStartsOn()).isEqualTo(entity.getStartsOn());
        assertThat(research.getEndsOn()).isEqualTo(entity.getEndsOn());
    }

    @Test
    public void givenAResearchEntityWithoutIdWhenMapThenReturnDomain() {
        var entity = ResearchEntity.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now())
                .build();

        var research = mapper.toDomain(entity);

        assertThat(research.getId()).isNull();
        assertThat(research.getTitle()).isEqualTo(entity.getTitle());
        assertThat(research.getDescription()).isEqualTo(entity.getDescription());
        assertThat(research.getStartsOn()).isEqualTo(entity.getStartsOn());
        assertThat(research.getEndsOn()).isEqualTo(entity.getEndsOn());
    }

}
