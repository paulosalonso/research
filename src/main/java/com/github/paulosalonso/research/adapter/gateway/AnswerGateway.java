package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.repository.AnswerRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.ResearchRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.AnswerSpecificationFactory;
import com.github.paulosalonso.research.adapter.mapper.AnswerMapper;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class AnswerGateway implements AnswerPort {

    private final AnswerRepository answerRepository;
    private final ResearchRepository researchRepository;
    private final AnswerSpecificationFactory specificationFactory;
    private final AnswerMapper mapper;

    @Override
    public Answer create(Answer answer) {
        return mapper.toDomain(answerRepository.save(mapper.toEntity(answer)));
    }

    @Override
    public List<Answer> search(AnswerCriteria answerCriteria) {
        if (!researchRepository.existsById(answerCriteria.getResearchId().toString())) {
            throw new NotFoundException();
        }

        return answerRepository.findAll(specificationFactory.findByAnswerCriteria(answerCriteria))
                .stream()
                .map(mapper::toDomain)
                .collect(toList());
    }
}
