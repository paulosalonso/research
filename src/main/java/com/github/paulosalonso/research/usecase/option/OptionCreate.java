package com.github.paulosalonso.research.usecase.option;

import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class OptionCreate {

    private final OptionPort optionPort;

    public Option create(UUID questionId, Option option) {
        option = option.toBuilder()
                .id(UUID.randomUUID())
                .build();

        return optionPort.create(questionId, option);
    }
}
