package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.model.AnswerEntity;
import com.github.paulosalonso.research.adapter.jpa.repository.AnswerRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.AnswerSpecificationFactory;
import com.github.paulosalonso.research.adapter.mapper.AnswerMapper;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnswerGatewayTest {

    @InjectMocks
    private AnswerGateway gateway;

    @Mock
    private AnswerRepository repository;

    @Mock
    private AnswerSpecificationFactory specificationFactory;

    @Mock
    private AnswerMapper mapper;

    @Test
    public void givenAnAnswerWhenCreateThenReturnCreatedAnswer() {
        var answer = Answer.builder()
                .date(OffsetDateTime.now())
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();

        var answerEntity = AnswerEntity.builder().build();

        when(mapper.toEntity(answer)).thenReturn(answerEntity);
        when(repository.save(answerEntity)).thenReturn(answerEntity);
        when(mapper.toDomain(answerEntity)).thenReturn(answer);

        assertThat(gateway.create(answer)).isSameAs(answer);

        verify(mapper).toEntity(answer);
        verify(repository).save(answerEntity);
        verify(mapper).toDomain(answerEntity);
    }

    @Test
    public void givenAnAnswerCriteriaWhenSearchThenReturnMappedResult() {
        var answer = Answer.builder()
                .date(OffsetDateTime.now())
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();

        var answerEntity = AnswerEntity.builder().build();
        var criteria = AnswerCriteria.builder().build();

        when(specificationFactory.findByAnswerCriteria(criteria)).thenCallRealMethod();
        when(repository.findAll(any(Specification.class))).thenReturn(List.of(answerEntity));
        when(mapper.toDomain(answerEntity)).thenReturn(answer);

        assertThat(gateway.search(criteria))
                .hasSize(1)
                .first()
                .isSameAs(answer);

        verify(specificationFactory).findByAnswerCriteria(criteria);
        verify(repository).findAll(any(Specification.class));
        verify(mapper).toDomain(answerEntity);
    }
}
