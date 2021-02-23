package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.mapper.QuestionMapper;
import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.adapter.jpa.repository.QuestionRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.ResearchRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.QuestionSpecificationFactory;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.ResearchSpecificationFactory;
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
    private final QuestionSpecificationFactory questionSpecificationFactory;
    private final ResearchSpecificationFactory researchSpecificationFactory;
    private final QuestionMapper mapper;

    @Transactional
    @Override
    public Question create(UUID researchId, String tenant, Question question) {
        var research = researchRepository.findOne(researchSpecificationFactory.findById(researchId.toString())
                .and(researchSpecificationFactory.findByTenant(tenant)))
                .orElseThrow(NotFoundException::new);

        var entity = mapper.toEntity(question);
        entity.setResearch(research);

        questionRepository.save(entity);

        return question;
    }

    @Override
    public Question read(UUID researchId, UUID questionId) {
        var specification = questionSpecificationFactory
                .findByResearchId(researchId.toString())
                .and(questionSpecificationFactory.findById(questionId.toString()));

        return questionRepository.findOne(specification)
                .map(question -> mapper.toDomain(question, false))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Question readFetchingOptions(UUID researchId, UUID questionId) {
        var specification = questionSpecificationFactory
                .findByResearchId(researchId.toString())
                .and(questionSpecificationFactory.findById(questionId.toString()))
                .and(questionSpecificationFactory.findFetchingOptions());

        return questionRepository.findOne(specification)
                .map(question -> mapper.toDomain(question, true))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Question> search(UUID researchId, QuestionCriteria criteria) {
        var specification = questionSpecificationFactory.findByResearchId(researchId.toString())
                .and(questionSpecificationFactory.findByQuestionCriteria(criteria))
                .and(orderByAsc(QuestionEntity.Fields.sequence));

        return questionRepository.findAll(specification)
                .stream()
                .map(question -> mapper.toDomain(question, false))
                .collect(toList());
    }

    @Transactional
    @Override
    public Question update(UUID researchId, Question question) {
        var specification = questionSpecificationFactory
                .findByResearchId(researchId.toString())
                .and(questionSpecificationFactory.findById(question.getId().toString()));

        questionRepository.findOne(specification)
                .map(entity -> mapper.copy(question, entity))
                .orElseThrow(NotFoundException::new);

        return question;
    }

    @Override
    public void delete(UUID researchId, UUID questionId) {
        var specification = questionSpecificationFactory
                .findByResearchId(researchId.toString())
                .and(questionSpecificationFactory.findById(questionId.toString()));

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
