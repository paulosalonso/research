package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.model.AnswerEntity;
import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.adapter.jpa.repository.AnswerRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.ResearchRepository;
import com.github.paulosalonso.research.adapter.mapper.AnswerMapper;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnswerGatewayTest {

    @InjectMocks
    private AnswerGateway gateway;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private ResearchRepository researchRepository;

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
        when(answerRepository.save(answerEntity)).thenReturn(answerEntity);
        when(mapper.toDomain(answerEntity)).thenReturn(answer);

        assertThat(gateway.create(answer)).isSameAs(answer);

        verify(mapper).toEntity(answer);
        verify(answerRepository).save(answerEntity);
        verify(mapper).toDomain(answerEntity);
    }

    @Test
    public void givenAnAnswerCriteriaWhenSearchThenReturnMappedResult() {
        var criteria = AnswerCriteria.builder()
                .dateFrom(OffsetDateTime.now())
                .dateTo(OffsetDateTime.now())
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .build();

        var research = ResearchEntity.builder()
                .id(UUID.randomUUID().toString())
                .build();

        when(researchRepository.findById(criteria.getResearchId().toString())).thenReturn(Optional.of(research));
        when(answerRepository.search(criteria)).thenReturn(emptyList());
        when(mapper.toDomain(research, emptyList())).thenCallRealMethod();

        gateway.search(criteria);

        verify(researchRepository).findById(criteria.getResearchId().toString());
        verify(answerRepository).search(criteria);
        verify(mapper).toDomain(research, emptyList());
    }

    @Test
    public void givenAnAnswerCriteriaWhenResearchIsNotFoundThenThrowsNotFoundException() {
        var criteria = AnswerCriteria.builder()
                .dateFrom(OffsetDateTime.now())
                .dateTo(OffsetDateTime.now())
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .build();

        when(researchRepository.findById(criteria.getResearchId().toString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gateway.search(criteria))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(researchRepository).findById(criteria.getResearchId().toString());
        verifyNoInteractions(answerRepository);
        verifyNoInteractions(mapper);
    }
}
