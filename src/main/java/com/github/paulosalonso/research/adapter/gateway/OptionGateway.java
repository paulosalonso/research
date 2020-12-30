package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.adapter.jpa.repository.OptionRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.QuestionRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.OptionSpecificationFactory;
import com.github.paulosalonso.research.adapter.mapper.OptionMapper;
import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.domain.OptionCriteria;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.github.paulosalonso.research.adapter.jpa.repository.specification.GeneralSpecificationFactory.orderByAsc;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class OptionGateway implements OptionPort {

    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;
    private final OptionSpecificationFactory specificationFactory;
    private final OptionMapper mapper;

    @Transactional
    @Override
    public Option create(UUID questionId, Option option) {
        var question = questionRepository.findById(questionId.toString())
                .orElseThrow(NotFoundException::new);

        var entity = mapper.toEntity(option);
        entity.setQuestion(question);

        optionRepository.save(entity);

        return option;
    }

    @Override
    public Option read(UUID questionId, UUID optionId) {
        var specification = specificationFactory
                .findById(optionId.toString())
                .and(specificationFactory.findByQuestionId(questionId.toString()));

        return optionRepository.findOne(specification)
                .map(mapper::toDomain)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Option> search(UUID questionId, OptionCriteria criteria) {
        var specification = specificationFactory.findByQuestionId(questionId.toString())
                .and(specificationFactory.findByOptionCriteria(criteria))
                .and(orderByAsc(OptionEntity.Fields.sequence));

        return optionRepository.findAll(specification)
                .stream()
                .map(mapper::toDomain)
                .collect(toList());
    }

    @Transactional
    @Override
    public Option update(UUID questionId, Option option) {
        var specification = specificationFactory
                .findByQuestionId(questionId.toString())
                .and(specificationFactory.findById(option.getId().toString()));

        optionRepository.findOne(specification)
                .map(entity -> mapper.copy(option, entity))
                .orElseThrow(NotFoundException::new);

        return option;
    }

    @Override
    public void delete(UUID questionId, UUID optionId) {
        var specification = specificationFactory
                .findByQuestionId(questionId.toString())
                .and(specificationFactory.findById(optionId.toString()));

        var option = optionRepository.findOne(specification)
                .orElseThrow(NotFoundException::new);

        optionRepository.delete(option);
    }
}
