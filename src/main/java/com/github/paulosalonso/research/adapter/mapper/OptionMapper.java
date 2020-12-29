package com.github.paulosalonso.research.adapter.mapper;

import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.domain.Option;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OptionMapper {

    public OptionEntity copy(Option from, OptionEntity to) {
        to.setDescription(from.getDescription());
        return to;
    }

    public Option toDomain(OptionEntity optionEntity) {
        UUID id = optionEntity.getId() != null ? UUID.fromString(optionEntity.getId()) : null;

        return Option.builder()
                .id(id)
                .sequence(optionEntity.getSequence())
                .description(optionEntity.getDescription())
                .build();
    }

    public OptionEntity toEntity(Option option) {
        String id = option.getId() != null ? option.getId().toString() : null;

        return OptionEntity.builder()
                .id(id)
                .sequence(option.getSequence())
                .description(option.getDescription())
                .build();
    }
}
