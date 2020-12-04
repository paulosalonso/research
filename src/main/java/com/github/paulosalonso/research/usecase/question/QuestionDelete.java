package com.github.paulosalonso.research.usecase.question;

import com.github.paulosalonso.research.usecase.port.QuestionPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class QuestionDelete {

    private final QuestionPort questionPort;

    public void delete(UUID researchId, UUID questionId) {
        questionPort.delete(researchId, questionId);
    }
}
