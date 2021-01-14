package com.github.paulosalonso.research.usecase.option;

import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.domain.OptionCriteria;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class OptionRead {

    private final OptionPort optionPort;

    public Option read(UUID questionId, UUID optionId, boolean fillQuestions) {
        if (fillQuestions) {
            return optionPort.readFetchingQuestions(questionId, optionId);
        }

        return optionPort.read(questionId, optionId);
    }

    public List<Option> search(UUID questionId, OptionCriteria criteria) {
        return optionPort.search(questionId, criteria);
    }
}
