package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.mapper.ResearchMapper;
import com.github.paulosalonso.research.adapter.jpa.repository.ResearchRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.ResearchSpecificationFactory;
import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.domain.ResearchCriteria;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class ResearchGateway implements ResearchPort {

    private final ResearchRepository repository;
    private final ResearchSpecificationFactory specificationFactory;
    private final ResearchMapper mapper;

    @Transactional
    @Override
    public Research create(Research research) {
        repository.save(mapper.toEntity(research));
        return research;
    }

    @Override
    public Research read(UUID id) {
        return repository.findById(id.toString())
                .map(research -> mapper.toDomain(research, false))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Research readFetchingQuestions(UUID id) {
        return repository.findAll(specificationFactory.findById(id.toString())
                .and(specificationFactory.findFetchingQuestions()))
                .stream()
                .findFirst()
                .map(research -> mapper.toDomain(research, true))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Research> search(ResearchCriteria criteria) {
        return repository.findAll(specificationFactory.findByResearchCriteria(criteria))
                .stream()
                .map(research -> mapper.toDomain(research, false))
                .collect(toList());
    }

    @Transactional
    @Override
    public Research update(Research research) {
        repository.findById(research.getId().toString())
                .map(persisted -> mapper.copy(research, persisted))
                .orElseThrow(NotFoundException::new);

        return research;
    }

    @Override
    public void delete(UUID id) {
        try {
            repository.deleteById(id.toString());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }
    }

    @Override
    public Integer getNextQuestionSequence(UUID researchId) {
        return repository.findLastQuestionSequence(researchId.toString())
                .map(sequence -> sequence + 1)
                .orElse(1);
    }
}
