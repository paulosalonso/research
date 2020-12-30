package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.adapter.jpa.repository.QuestionRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.ResearchRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.QuestionSpecificationFactory;
import com.github.paulosalonso.research.adapter.mapper.QuestionMapper;
import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.domain.QuestionCriteria;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.github.paulosalonso.research.adapter.jpa.repository.specification.GeneralSpecificationFactory.orderByAsc;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class QuestionGateway implements QuestionPort {

    private final ResearchRepository researchRepository;
    private final QuestionRepository questionRepository;
    private final QuestionSpecificationFactory specificationFactory;
    private final QuestionMapper mapper;

    @Transactional
    @Override
    public Question create(UUID researchId, Question question) {
        var research = researchRepository.findById(researchId.toString())
                .orElseThrow(NotFoundException::new);

        var entity = mapper.toEntity(question);
        entity.setResearch(research);

        questionRepository.save(entity);

        return question;
    }

    @Override
    public Question read(UUID researchId, UUID questionId) {
        var specification = specificationFactory
                .findByResearchId(researchId.toString())
                .and(specificationFactory.findById(questionId.toString()));

        return questionRepository.findOne(specification)
                .map(mapper::toDomain)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Question> search(UUID researchId, QuestionCriteria criteria) {
        var specification = specificationFactory.findByResearchId(researchId.toString())
                .and(specificationFactory.findByQuestionCriteria(criteria))
                .and(orderByAsc(QuestionEntity.Fields.sequence));

        return questionRepository.findAll(specification)
                .stream()
                .map(mapper::toDomain)
                .collect(toList());
    }

    @Transactional
    @Override
    public Question update(UUID researchId, Question question) {
        var specification = specificationFactory
                .findByResearchId(researchId.toString())
                .and(specificationFactory.findById(question.getId().toString()));

        questionRepository.findOne(specification)
                .map(entity -> mapper.copy(question, entity))
                .orElseThrow(NotFoundException::new);

        return question;
    }

    @Override
    public void delete(UUID researchId, UUID questionId) {
        var specification = specificationFactory
                .findByResearchId(researchId.toString())
                .and(specificationFactory.findById(questionId.toString()));

        var question = questionRepository.findOne(specification)
                .orElseThrow(NotFoundException::new);

        questionRepository.delete(question);
    }

    @Override
    public Integer getNextOptionSequence(UUID questionId) {
        return questionRepository.findLastOptionSequence(questionId.toString())
                .map(sequence -> sequence + 1)
                .orElse(1);
    }
}
