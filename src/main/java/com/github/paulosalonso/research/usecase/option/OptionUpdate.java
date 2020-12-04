package com.github.paulosalonso.research.usecase.option;

import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class OptionUpdate {

    private final OptionPort optionPort;

    public Option update(UUID questionId, Option option) {
        return optionPort.update(questionId, option);
    }
}
