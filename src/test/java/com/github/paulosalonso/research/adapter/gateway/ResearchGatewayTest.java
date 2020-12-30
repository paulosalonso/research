package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.mapper.ResearchMapper;
import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.adapter.jpa.repository.ResearchRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.ResearchSpecificationFactory;
import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.domain.ResearchCriteria;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class ResearchGatewayTest {

    @InjectMocks
    private ResearchGateway gateway;

    @Mock
    private ResearchRepository repository;

    @Mock
    private ResearchSpecificationFactory specificationFactory;

    @Mock
    private ResearchMapper mapper;

    @Test
    public void givenAResearchWhenCreateThenGenerateIdAndPersist() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .build();

        gateway.create(research);

        verify(mapper).toEntity(research);
        verifyNoMoreInteractions(mapper);
        verify(repository).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenAnIdWhenReadThenReturnResearch() {
        var id = UUID.randomUUID();

        var entity = ResearchEntity.builder()
                .id(id.toString())
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        when(repository.findById(id.toString())).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenCallRealMethod();

        gateway.read(id);

        verify(repository).findById(id.toString());
        verifyNoMoreInteractions(repository);
        verify(mapper).toDomain(entity);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void givenANonexistentIdWhenReadThenThrowsNotFoundException() {
        var id = UUID.randomUUID();

        when(repository.findById(id.toString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gateway.read(id))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(repository).findById(id.toString());
        verifyNoInteractions(mapper);
    }

    @Test
    public void givenAResearchCriteriaWhenSearchThenCallRepository() {
        var criteria = ResearchCriteria.builder().build();
        var entity = ResearchEntity.builder()
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        when(specificationFactory.findByResearchCriteria(criteria)).thenCallRealMethod();
        when(mapper.toDomain(any(ResearchEntity.class))).thenCallRealMethod();
        when(repository.findAll(any(Specification.class))).thenReturn(List.of(entity));

        gateway.search(criteria);

        verify(specificationFactory).findByResearchCriteria(criteria);
        verifyNoMoreInteractions(specificationFactory);
        verify(repository).findAll(any(Specification.class));
        verifyNoMoreInteractions(repository);
        verify(mapper).toDomain(entity);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void givenAResearchWhenUpdateThenFindAndCopy() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .title("title b")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusHours(15))
                .build();

        var entity = ResearchEntity.builder()
                .id(research.getId().toString())
                .title("title a")
                .description("description a")
                .startsOn(OffsetDateTime.now().minusHours(2))
                .endsOn(OffsetDateTime.now().plusDays(10))
                .build();

        when(repository.findById(research.getId().toString())).thenReturn(Optional.of(entity));
        when(mapper.copy(research, entity)).thenCallRealMethod();

        gateway.update(research);

        verify(repository).findById(research.getId().toString());
        verifyNoMoreInteractions(repository);
        verify(mapper).copy(research, entity);
        verifyNoMoreInteractions(mapper);

        assertThat(entity.getTitle()).isEqualTo(research.getTitle());
        assertThat(entity.getDescription()).isEqualTo(research.getDescription());
        assertThat(entity.getStartsOn()).isEqualTo(research.getStartsOn());
        assertThat(entity.getEndsOn()).isEqualTo(research.getEndsOn());
    }

    @Test
    public void givenAResearchWithNonexistentIdWhenUpdateThenThrowsNotFoundException() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        when(repository.findById(research.getId().toString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gateway.update(research))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(repository).findById(research.getId().toString());
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    public void givenAnIdWhenDeleteThenCallRepository() {
        var id = UUID.randomUUID();

        gateway.delete(id);

        verify(repository).deleteById(id.toString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenAResearchWithoutQuestionsWhenGetNextQuestionSequenceThenReturnOne() {
        var id = UUID.randomUUID();

        when(repository.findLastQuestionSequence(id.toString())).thenReturn(Optional.empty());

        var sequence = gateway.getNextQuestionSequence(id);

        assertThat(sequence).isEqualTo(1);
        verify(repository).findLastQuestionSequence(id.toString());
    }

    @Test
    public void givenAResearchWithOptionsWhenGetNextQuestionSequenceThenReturnNextSequence() {
        var id = UUID.randomUUID();

        when(repository.findLastQuestionSequence(id.toString())).thenReturn(Optional.of(1));

        var sequence = gateway.getNextQuestionSequence(id);

        assertThat(sequence).isEqualTo(2);
        verify(repository).findLastQuestionSequence(id.toString());
    }
}
