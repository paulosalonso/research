package com.github.paulosalonso.research.usecase.port;

import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.domain.OptionCriteria;

import java.util.List;
import java.util.UUID;

public interface OptionPort {

    Option create(UUID questionId, Option option);
    Option read(UUID questionId, UUID optionId);
    Option readFetchingQuestions(UUID questionId, UUID optionId);
    List<Option> search(UUID questionId, OptionCriteria criteria);
    Option update(UUID questionId, Option option);
    void delete(UUID questionId, UUID optionId);
}
