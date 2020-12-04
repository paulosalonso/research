package com.github.paulosalonso.research.usecase.option;

import com.github.paulosalonso.research.usecase.port.OptionPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class OptionDelete {

    private final OptionPort optionPort;

    public void delete(UUID questionId, UUID optionId) {
        optionPort.delete(questionId, optionId);
    }
}
