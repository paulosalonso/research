package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.repository.AnswerRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.ResearchRepository;
import com.github.paulosalonso.research.adapter.mapper.AnswerMapper;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import com.github.paulosalonso.research.domain.ResearchSummary;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerGateway implements AnswerPort {

    private final AnswerRepository answerRepository;
    private final ResearchRepository researchRepository;
    private final AnswerMapper mapper;

    @Override
    public Answer create(Answer answer) {
        return mapper.toDomain(answerRepository.save(mapper.toEntity(answer)));
    }

    @Override
    public ResearchSummary search(AnswerCriteria answerCriteria) {
        var research = researchRepository
                .findById(answerCriteria.getResearchId().toString())
                .orElseThrow(NotFoundException::new);

        return mapper.toDomain(research, answerRepository.search(answerCriteria));
    }
}
