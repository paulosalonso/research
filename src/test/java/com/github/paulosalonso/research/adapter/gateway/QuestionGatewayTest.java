package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.adapter.jpa.repository.QuestionRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.ResearchRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.QuestionSpecificationFactory;
import com.github.paulosalonso.research.adapter.mapper.QuestionMapper;
import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.domain.QuestionCriteria;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionGatewayTest {

    @InjectMocks
    private QuestionGateway gateway;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ResearchRepository researchRepository;

    @Mock
    private QuestionSpecificationFactory specificationFactory;

    @Mock
    private QuestionMapper mapper;

    @Test
    public void givenAQuestionWhenCreateThenMapAndSaveIt() {
        var researchId = UUID.randomUUID();

        var research = ResearchEntity.builder()
                .id(researchId.toString())
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        var question = Question.builder()
                .id(UUID.randomUUID())
                .description("description")
                .multiSelect(true)
                .build();

        when(researchRepository.findById(research.getId())).thenReturn(Optional.of(research));
        when(mapper.toEntity(question)).thenCallRealMethod();

        gateway.create(researchId, question);

        verify(researchRepository).findById(researchId.toString());
        verifyNoMoreInteractions(researchRepository);
        verify(mapper).toEntity(question);
        verifyNoMoreInteractions(mapper);

        var questionCaptor = ArgumentCaptor.forClass(QuestionEntity.class);
        verify(questionRepository).save(questionCaptor.capture());
        verifyNoMoreInteractions(questionRepository);

        assertThat(questionCaptor.getValue().getResearch()).isSameAs(research);
    }

    @Test
    public void givenAResearchIdAndAQuestionIdWhenReadThenFindAndMapIt() {
        var researchId = UUID.randomUUID();
        var questionId = UUID.randomUUID();

        var entity = QuestionEntity.builder()
                .id(questionId.toString())
                .description("description")
                .multiSelect(true)
                .build();

        when(specificationFactory.findByResearchId(researchId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(questionId.toString())).thenCallRealMethod();
        when(questionRepository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenCallRealMethod();

        gateway.read(researchId, questionId);

        verify(specificationFactory).findByResearchId(researchId.toString());
        verify(specificationFactory).findById(questionId.toString());
        verifyNoMoreInteractions(specificationFactory);
        verify(questionRepository).findOne(any(Specification.class));
        verifyNoMoreInteractions(questionRepository);
        verify(mapper).toDomain(entity);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void givenANonexistentCompositeIdWhenReadThenThrowsNotFoundException() {
        var researchId = UUID.randomUUID();
        var questionId = UUID.randomUUID();

        when(specificationFactory.findByResearchId(researchId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(questionId.toString())).thenCallRealMethod();
        when(questionRepository.findOne(any(Specification.class))).thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> gateway.read(researchId, questionId))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(specificationFactory).findByResearchId(researchId.toString());
        verify(specificationFactory).findById(questionId.toString());
        verifyNoMoreInteractions(specificationFactory);
        verify(questionRepository).findOne(any(Specification.class));
        verifyNoInteractions(mapper);
    }

    @Test
    public void givenAResearchIdAndAQuestionCriteriaWhenSearchThenFindAndMapIt() {
        var researchId = UUID.randomUUID();
        var criteria = QuestionCriteria.builder().build();
        var entity = QuestionEntity.builder()
                .description("description")
                .multiSelect(true)
                .build();

        when(specificationFactory.findByResearchId(researchId.toString())).thenCallRealMethod();
        when(specificationFactory.findByQuestionCriteria(criteria)).thenCallRealMethod();
        when(mapper.toDomain(any(QuestionEntity.class))).thenCallRealMethod();
        when(questionRepository.findAll(any(Specification.class))).thenReturn(List.of(entity));

        gateway.search(researchId, criteria);

        verify(specificationFactory).findByResearchId(researchId.toString());
        verify(specificationFactory).findByQuestionCriteria(criteria);
        verifyNoMoreInteractions(specificationFactory);
        verify(questionRepository).findAll(any(Specification.class));
        verifyNoMoreInteractions(questionRepository);
        verify(mapper).toDomain(entity);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void givenAResearchIdAndAQuestionWhenUpdateThenFindAndCopyIt() {
        var researchId = UUID.randomUUID();

        var question = Question.builder()
                .id(UUID.randomUUID())
                .description("description b")
                .multiSelect(false)
                .build();

        var entity = QuestionEntity.builder()
                .id(question.getId().toString())
                .description("description a")
                .multiSelect(true)
                .build();

        when(specificationFactory.findByResearchId(researchId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(question.getId().toString())).thenCallRealMethod();
        when(questionRepository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(mapper.copy(question, entity)).thenCallRealMethod();

        gateway.update(researchId, question);

        verify(specificationFactory).findByResearchId(researchId.toString());
        verify(specificationFactory).findById(question.getId().toString());
        verifyNoMoreInteractions(specificationFactory);
        verify(questionRepository).findOne(any(Specification.class));
        verifyNoMoreInteractions(questionRepository);
        verify(mapper).copy(question, entity);
        verifyNoMoreInteractions(mapper);

        assertThat(entity.getDescription()).isEqualTo(question.getDescription());
        assertThat(entity.getMultiSelect()).isEqualTo(question.getMultiSelect());
    }

    @Test
    public void givenAResearchIdAndAQuestionWithNonexistentCompositeIdWhenUpdateThenThrowsNotFoundException() {
        var researchId = UUID.randomUUID();

        var question = Question.builder()
                .id(UUID.randomUUID())
                .description("description")
                .multiSelect(true)
                .build();

        when(specificationFactory.findByResearchId(researchId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(question.getId().toString())).thenCallRealMethod();
        when(questionRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gateway.update(researchId, question))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(specificationFactory).findByResearchId(researchId.toString());
        verify(specificationFactory).findById(question.getId().toString());
        verifyNoMoreInteractions(specificationFactory);
        verify(questionRepository).findOne(any(Specification.class));
        verifyNoMoreInteractions(questionRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    public void givenAResearchIdAndAQuestionIdWhenDeleteThenFindAndDeleteIt() {
        var researchId = UUID.randomUUID();
        var questionId = UUID.randomUUID();

        var question = QuestionEntity.builder()
                .id(questionId.toString())
                .description("description")
                .build();

        when(specificationFactory.findByResearchId(researchId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(questionId.toString())).thenCallRealMethod();
        when(questionRepository.findOne(any(Specification.class))).thenReturn(Optional.of(question));

        gateway.delete(researchId, questionId);

        verify(specificationFactory).findByResearchId(researchId.toString());
        verify(specificationFactory).findById(question.getId().toString());
        verifyNoMoreInteractions(specificationFactory);
        verify(questionRepository).delete(question);
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    public void givenANonexistentCompositeIdWhenDeleteThenThrowsNotFoundException() {
        var researchId = UUID.randomUUID();
        var questionId = UUID.randomUUID();

        var question = QuestionEntity.builder()
                .id(questionId.toString())
                .description("description")
                .multiSelect(false)
                .build();

        when(specificationFactory.findByResearchId(researchId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(questionId.toString())).thenCallRealMethod();
        when(questionRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gateway.delete(researchId, questionId))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(specificationFactory).findByResearchId(researchId.toString());
        verify(specificationFactory).findById(question.getId());
        verifyNoMoreInteractions(specificationFactory);
        verify(questionRepository, never()).delete(question);
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    public void givenAQuestionWithoutOptionsWhenGetNextOptionSequenceThenReturnOne() {
        var id = UUID.randomUUID();

        when(questionRepository.findLastOptionSequence(id.toString())).thenReturn(Optional.empty());

        var sequence = gateway.getNextOptionSequence(id);

        assertThat(sequence).isEqualTo(1);
        verify(questionRepository).findLastOptionSequence(id.toString());
    }

    @Test
    public void givenAQuestionWithOptionsWhenGetNextOptionSequenceThenReturnNextSequence() {
        var id = UUID.randomUUID();

        when(questionRepository.findLastOptionSequence(id.toString())).thenReturn(Optional.of(1));

        var sequence = gateway.getNextOptionSequence(id);

        assertThat(sequence).isEqualTo(2);
        verify(questionRepository).findLastOptionSequence(id.toString());
    }
}
