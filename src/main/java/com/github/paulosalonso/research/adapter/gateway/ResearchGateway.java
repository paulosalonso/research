package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.mapper.QuestionMapper;
import com.github.paulosalonso.research.adapter.jpa.mapper.ResearchMapper;
import com.github.paulosalonso.research.adapter.jpa.repository.QuestionRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.ResearchRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.QuestionSpecificationFactory;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.ResearchSpecificationFactory;
import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.domain.ResearchCriteria;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Service
public class ResearchGateway implements ResearchPort {

    private final ResearchRepository researchRepository;
    private final QuestionRepository questionRepository;
    private final ResearchSpecificationFactory researchSpecificationFactory;
    private final QuestionSpecificationFactory questionSpecificationFactory;
    private final ResearchMapper researchMapper;
    private final QuestionMapper questionMapper;

    @Transactional
    @Override
    public Research create(Research research) {
        researchRepository.save(researchMapper.toEntity(research));
        return research;
    }

    @Override
    public Research read(UUID id, String tenant) {
        return researchRepository.findOne(researchSpecificationFactory.findById(id.toString()).and(researchSpecificationFactory.findByTenant(tenant)))
                .map(research -> researchMapper.toDomain(research, false))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Research readFetchingQuestions(UUID id, String tenant) {
        var research = researchRepository.findOne(researchSpecificationFactory.findById(id.toString()).and(researchSpecificationFactory.findByTenant(tenant)))
                .map(entity -> researchMapper.toDomain(entity, true))
                .orElseThrow(NotFoundException::new);

        var specification = questionSpecificationFactory.findByResearchId(id.toString());
        specification = specification.and(questionSpecificationFactory.findFetchingOptions());

        var questions = questionRepository.findAll(specification).stream()
                .map(question -> questionMapper.toDomain(question, true))
                .collect(toSet());

        return research.toBuilder()
                .questions(questions)
                .build();
    }

    @Override
    public List<Research> search(ResearchCriteria criteria) {
        return researchRepository.findAll(researchSpecificationFactory.findByResearchCriteria(criteria))
                .stream()
                .map(research -> researchMapper.toDomain(research, false))
                .collect(toList());
    }

    @Transactional
    @Override
    public Research update(Research research) {
        researchRepository.findOne(researchSpecificationFactory.findById(research.getId().toString()).and(researchSpecificationFactory.findByTenant(research.getTenant())))
                .map(persisted -> researchMapper.copy(research, persisted))
                .orElseThrow(NotFoundException::new);

        return research;
    }

    @Transactional
    @Override
    public void delete(UUID id, String tenant) {
        if (researchRepository.deleteByIdAndTenant(id.toString(), tenant) == 0) {
            throw new NotFoundException();
        }
    }

    @Override
    public Integer getNextQuestionSequence(UUID researchId) {
        return researchRepository.findLastQuestionSequence(researchId.toString())
                .map(sequence -> sequence + 1)
                .orElse(1);
    }
}
