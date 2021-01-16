package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.mapper.OptionMapper;
import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.adapter.jpa.repository.OptionRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.QuestionRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.OptionSpecificationFactory;
import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.domain.OptionCriteria;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OptionGatewayTest {

    @InjectMocks
    private OptionGateway gateway;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private OptionSpecificationFactory specificationFactory;

    @Mock
    private OptionMapper mapper;

    @Test
    public void givenAnOptionWhenCreateThenMapAndSaveIt() {
        var questionId = UUID.randomUUID();

        var question = QuestionEntity.builder()
                .id(questionId.toString())
                .description("description")
                .multiSelect(false)
                .build();

        var option = Option.builder()
                .id(UUID.randomUUID())
                .description("description")
                .build();

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        when(mapper.toEntity(option)).thenCallRealMethod();

        gateway.create(questionId, option);

        verify(questionRepository).findById(questionId.toString());
        verifyNoMoreInteractions(questionRepository);
        verify(mapper).toEntity(option);
        verifyNoMoreInteractions(mapper);

        var questionCaptor = ArgumentCaptor.forClass(OptionEntity.class);
        verify(optionRepository).save(questionCaptor.capture());
        verifyNoMoreInteractions(optionRepository);

        assertThat(questionCaptor.getValue().getQuestion()).isSameAs(question);
    }

    @Test
    public void givenAQuestionIdAndAnOptionIdWhenReadThenFindAndMapIt() {
        var questionId = UUID.randomUUID();
        var optionId = UUID.randomUUID();

        var entity = OptionEntity.builder()
                .id(optionId.toString())
                .description("description")
                .build();

        when(specificationFactory.findByQuestionId(questionId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(optionId.toString())).thenCallRealMethod();
        when(optionRepository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenCallRealMethod();

        gateway.read(questionId, optionId);

        verify(specificationFactory).findByQuestionId(questionId.toString());
        verify(specificationFactory).findById(optionId.toString());
        verifyNoMoreInteractions(specificationFactory);
        verify(optionRepository).findOne(any(Specification.class));
        verifyNoMoreInteractions(optionRepository);
        verify(mapper).toDomain(entity);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void givenANonexistentCompositeIdWhenReadThenThrowsNotFoundException() {
        var questionId = UUID.randomUUID();
        var optionId = UUID.randomUUID();

        when(specificationFactory.findByQuestionId(questionId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(optionId.toString())).thenCallRealMethod();
        when(optionRepository.findOne(any(Specification.class))).thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> gateway.read(questionId, optionId))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(specificationFactory).findByQuestionId(questionId.toString());
        verify(specificationFactory).findById(optionId.toString());
        verifyNoMoreInteractions(specificationFactory);
        verify(optionRepository).findOne(any(Specification.class));
        verifyNoInteractions(mapper);
    }

    @Test
    public void givenAQuestionIdAndAnOptionCriteriaWhenSearchThenFindAndMapIt() {
        var questionId = UUID.randomUUID();
        var criteria = OptionCriteria.builder().build();
        var entity = OptionEntity.builder()
                .description("description")
                .build();

        when(specificationFactory.findByQuestionId(questionId.toString())).thenCallRealMethod();
        when(specificationFactory.findByOptionCriteria(criteria)).thenCallRealMethod();
        when(mapper.toDomain(any(OptionEntity.class))).thenCallRealMethod();
        when(optionRepository.findAll(any(Specification.class))).thenReturn(List.of(entity));

        gateway.search(questionId, criteria);

        verify(specificationFactory).findByQuestionId(questionId.toString());
        verify(specificationFactory).findByOptionCriteria(criteria);
        verifyNoMoreInteractions(specificationFactory);
        verify(optionRepository).findAll(any(Specification.class));
        verifyNoMoreInteractions(optionRepository);
        verify(mapper).toDomain(entity);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void givenAQuestionIdAndAnOptionWhenUpdateThenFindAndCopyIt() {
        var questionId = UUID.randomUUID();

        var option = Option.builder()
                .id(UUID.randomUUID())
                .description("description b")
                .build();

        var entity = OptionEntity.builder()
                .id(option.getId().toString())
                .description("description a")
                .build();

        when(specificationFactory.findByQuestionId(questionId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(option.getId().toString())).thenCallRealMethod();
        when(optionRepository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(mapper.copy(option, entity)).thenCallRealMethod();

        gateway.update(questionId, option);

        verify(specificationFactory).findByQuestionId(questionId.toString());
        verify(specificationFactory).findById(option.getId().toString());
        verifyNoMoreInteractions(specificationFactory);
        verify(optionRepository).findOne(any(Specification.class));
        verifyNoMoreInteractions(optionRepository);
        verify(mapper).copy(option, entity);
        verifyNoMoreInteractions(mapper);

        assertThat(entity.getDescription()).isEqualTo(option.getDescription());
    }

    @Test
    public void givenAQuestionIdAndAOptionWithNonexistentCompositeIdWhenUpdateThenThrowsNotFoundException() {
        var questionId = UUID.randomUUID();

        var option = Option.builder()
                .id(UUID.randomUUID())
                .description("description")
                .build();

        when(specificationFactory.findByQuestionId(questionId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(option.getId().toString())).thenCallRealMethod();
        when(optionRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gateway.update(questionId, option))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(specificationFactory).findByQuestionId(questionId.toString());
        verify(specificationFactory).findById(option.getId().toString());
        verifyNoMoreInteractions(specificationFactory);
        verify(optionRepository).findOne(any(Specification.class));
        verifyNoMoreInteractions(optionRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    public void givenAQuestionIdAndAnOptionIdWhenDeleteThenFindAndDeleteIt() {
        var questionId = UUID.randomUUID();
        var optionId = UUID.randomUUID();

        var option = OptionEntity.builder()
                .id(optionId.toString())
                .description("description")
                .build();

        when(specificationFactory.findByQuestionId(questionId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(optionId.toString())).thenCallRealMethod();
        when(optionRepository.findOne(any(Specification.class))).thenReturn(Optional.of(option));

        gateway.delete(questionId, optionId);

        verify(specificationFactory).findByQuestionId(questionId.toString());
        verify(specificationFactory).findById(option.getId());
        verifyNoMoreInteractions(specificationFactory);
        verify(optionRepository).delete(option);
        verifyNoMoreInteractions(optionRepository);
    }

    @Test
    public void givenANonexistentCompositeIdWhenDeleteThenThrowsNotFoundException() {
        var questionId = UUID.randomUUID();
        var optionId = UUID.randomUUID();

        var option = OptionEntity.builder()
                .id(optionId.toString())
                .description("description")
                .build();

        when(specificationFactory.findByQuestionId(questionId.toString())).thenCallRealMethod();
        when(specificationFactory.findById(optionId.toString())).thenCallRealMethod();
        when(optionRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gateway.delete(questionId, optionId))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(specificationFactory).findByQuestionId(questionId.toString());
        verify(specificationFactory).findById(option.getId());
        verifyNoMoreInteractions(specificationFactory);
        verify(optionRepository, never()).delete(option);
        verifyNoMoreInteractions(optionRepository);
    }
}
