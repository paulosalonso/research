package com.github.paulosalonso.research.adapter.controller.mapper;

import com.github.paulosalonso.research.adapter.controller.dto.OptionCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.OptionDTO;
import com.github.paulosalonso.research.adapter.controller.dto.OptionInputDTO;
import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.domain.OptionCriteria;
import org.springframework.stereotype.Component;

@Component
public class OptionDTOMapper {

    public OptionDTO toDTO(Option option) {
        return OptionDTO.builder()
                .id(option.getId())
                .sequence(option.getSequence())
                .description(option.getDescription())
                .notify(option.isNotify())
                .build();
    }

    public Option toDomain(OptionInputDTO dto) {
        return Option.builder()
                .description(dto.getDescription())
                .notify(dto.isNotify())
                .build();
    }

    public OptionCriteria toDomain(OptionCriteriaDTO dto) {
        return OptionCriteria.builder()
                .description(dto.getDescription())
                .notify(dto.getNotify())
                .build();
    }
}
