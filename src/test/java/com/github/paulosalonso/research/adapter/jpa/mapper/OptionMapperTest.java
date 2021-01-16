package com.github.paulosalonso.research.adapter.jpa.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.domain.Option;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionMapperTest {

    private final OptionMapper mapper = new OptionMapper();

    @Test
    public void givenAnOptionWhenMapThenReturnEntity() {
        var option = Option.builder()
                .id(UUID.randomUUID())
                .description("description")
                .notify(true)
                .build();

        var entity = mapper.toEntity(option);

        assertThat(entity.getId()).isEqualTo(option.getId().toString());
        assertThat(entity.getDescription()).isEqualTo(option.getDescription());
        assertThat(entity.isNotify()).isEqualTo(option.isNotify());
    }

    @Test
    public void givenAnOptionWithoutIdWhenMapThenReturnEntity() {
        var option = Option.builder()
                .description("description")
                .notify(true)
                .build();

        var entity = mapper.toEntity(option);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getDescription()).isEqualTo(option.getDescription());
        assertThat(entity.isNotify()).isEqualTo(option.isNotify());
    }

    @Test
    public void givenAnOptionEntityWhenMapThenReturnDomain() {
        var entity = OptionEntity.builder()
                .id(UUID.randomUUID().toString())
                .description("description")
                .notify(true)
                .build();

        var option = mapper.toDomain(entity);

        assertThat(option.getId()).isEqualTo(UUID.fromString(entity.getId()));
        assertThat(option.getDescription()).isEqualTo(entity.getDescription());
        assertThat(option.isNotify()).isEqualTo(entity.isNotify());
    }

    @Test
    public void givenAnOptionEntityWithoutIdWhenMapThenReturnDomain() {
        var entity = OptionEntity.builder()
                .description("description")
                .notify(true)
                .build();

        var option = mapper.toDomain(entity);

        assertThat(option.getId()).isNull();
        assertThat(option.getDescription()).isEqualTo(entity.getDescription());
        assertThat(option.isNotify()).isEqualTo(entity.isNotify());
    }

}
