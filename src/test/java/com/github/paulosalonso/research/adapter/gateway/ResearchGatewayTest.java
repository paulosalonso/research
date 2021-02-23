package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.jpa.mapper.QuestionMapper;
import com.github.paulosalonso.research.adapter.jpa.mapper.ResearchMapper;
import com.github.paulosalonso.research.adapter.jpa.model.OptionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.QuestionEntity;
import com.github.paulosalonso.research.adapter.jpa.model.ResearchEntity;
import com.github.paulosalonso.research.adapter.jpa.repository.QuestionRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.ResearchRepository;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.QuestionSpecificationFactory;
import com.github.paulosalonso.research.adapter.jpa.repository.specification.ResearchSpecificationFactory;
import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.domain.ResearchCriteria;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
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

    private static final String TENANT = "tenant";

    @InjectMocks
    private ResearchGateway gateway;

    @Mock
    private ResearchRepository researchRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ResearchSpecificationFactory researchSpecificationFactory;

    @Mock
    private QuestionSpecificationFactory questionSpecificationFactory;

    @Mock
    private ResearchMapper researchMapper;

    @Mock
    private QuestionMapper questionMapper;

    @BeforeEach
    public void setUp() {
        lenient().when(researchSpecificationFactory.findById(anyString())).thenCallRealMethod();
        lenient().when(researchSpecificationFactory.findByTenant(TENANT)).thenCallRealMethod();
    }

    @Test
    public void givenAResearchWhenCreateThenGenerateIdAndPersist() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .build();

        gateway.create(research);

        verify(researchMapper).toEntity(research);
        verifyNoMoreInteractions(researchMapper);
        verify(researchRepository).save(any());
        verifyNoMoreInteractions(researchRepository);
    }

    @Test
    public void givenAnIdWhenReadWithoutQuestionsThenReturnResearch() {
        var id = UUID.randomUUID();

        var entity = ResearchEntity.builder()
                .id(id.toString())
                .tenant(TENANT)
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        when(researchRepository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(researchMapper.toDomain(entity, false)).thenCallRealMethod();

        gateway.read(id, TENANT);

        verify(researchSpecificationFactory).findById(id.toString());
        verify(researchSpecificationFactory).findByTenant(TENANT);
        verify(researchRepository).findOne(any(Specification.class));
        verify(researchSpecificationFactory).findById(id.toString());
        verify(researchSpecificationFactory).findByTenant(TENANT);
        verify(researchMapper).toDomain(entity, false);
    }

    @Test
    public void givenAnIdWhenReadWithQuestionsThenReturnResearch() {
        var id = UUID.randomUUID();

        var option = OptionEntity.builder().build();
        var question = QuestionEntity.builder()
                .options(List.of(option))
                .build();
        var entity = ResearchEntity.builder()
                .id(id.toString())
                .tenant(TENANT)
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        var result = Research.builder()
                .id(UUID.fromString(entity.getId()))
                .tenant(entity.getTenant())
                .title(entity.getTitle())
                .startsOn(entity.getStartsOn())
                .build();

        when(questionSpecificationFactory.findByResearchId(id.toString())).thenCallRealMethod();
        when(questionSpecificationFactory.findFetchingOptions()).thenCallRealMethod();
        when(researchRepository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(questionRepository.findAll(any(Specification.class))).thenReturn(List.of(question));
        when(researchMapper.toDomain(entity, true)).thenReturn(result);

        gateway.readFetchingQuestions(id, TENANT);

        verify(researchSpecificationFactory).findById(id.toString());
        verify(researchSpecificationFactory).findByTenant(TENANT);
        verify(researchRepository).findOne(any(Specification.class));
        verify(questionSpecificationFactory).findByResearchId(id.toString());
        verify(questionSpecificationFactory).findFetchingOptions();
        verify(questionRepository).findAll(any(Specification.class));
        verify(researchMapper).toDomain(entity, true);
        verify(questionMapper).toDomain(question, true);
    }

    @Test
    public void givenANonexistentIdWhenReadThenThrowsNotFoundException() {
        var id = UUID.randomUUID();

        when(researchRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gateway.read(id, TENANT))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(researchSpecificationFactory).findById(id.toString());
        verify(researchSpecificationFactory).findByTenant(TENANT);
        verify(researchRepository).findOne(any(Specification.class));
        verifyNoInteractions(researchMapper);
    }

    @Test
    public void givenAResearchCriteriaWhenSearchThenCallRepository() {
        var criteria = ResearchCriteria.builder()
                .tenant(TENANT)
                .build();
        var entity = ResearchEntity.builder()
                .id(UUID.randomUUID().toString())
                .tenant(TENANT)
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        when(researchSpecificationFactory.findByResearchCriteria(criteria)).thenCallRealMethod();
        when(researchMapper.toDomain(any(ResearchEntity.class), eq(false))).thenCallRealMethod();
        when(researchRepository.findAll(any(Specification.class))).thenReturn(List.of(entity));

        assertThat(gateway.search(criteria)).hasSize(1);

        verify(researchSpecificationFactory).findByResearchCriteria(criteria);
        verify(researchRepository).findAll(any(Specification.class));
        verify(researchMapper).toDomain(entity, false);
    }

    @Test
    public void givenAResearchWhenUpdateThenFindAndCopy() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .tenant(TENANT)
                .title("title b")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusHours(15))
                .build();

        var entity = ResearchEntity.builder()
                .id(research.getId().toString())
                .tenant(TENANT)
                .title("title a")
                .description("description a")
                .startsOn(OffsetDateTime.now().minusHours(2))
                .endsOn(OffsetDateTime.now().plusDays(10))
                .build();

        when(researchSpecificationFactory.findById(research.getId().toString())).thenCallRealMethod();
        when(researchSpecificationFactory.findByTenant(TENANT)).thenCallRealMethod();
        when(researchRepository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(researchMapper.copy(research, entity)).thenCallRealMethod();

        gateway.update(research);

        verify(researchSpecificationFactory).findById(research.getId().toString());
        verify(researchSpecificationFactory).findByTenant(TENANT);
        verify(researchRepository).findOne(any(Specification.class));
        verify(researchMapper).copy(research, entity);

        assertThat(entity.getTitle()).isEqualTo(research.getTitle());
        assertThat(entity.getDescription()).isEqualTo(research.getDescription());
        assertThat(entity.getStartsOn()).isEqualTo(research.getStartsOn());
        assertThat(entity.getEndsOn()).isEqualTo(research.getEndsOn());
    }

    @Test
    public void givenAResearchWithNonexistentIdWhenUpdateThenThrowsNotFoundException() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .tenant(TENANT)
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        when(researchSpecificationFactory.findById(research.getId().toString())).thenCallRealMethod();
        when(researchSpecificationFactory.findByTenant(TENANT)).thenCallRealMethod();
        when(researchRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gateway.update(research))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(researchSpecificationFactory).findById(research.getId().toString());
        verify(researchSpecificationFactory).findByTenant(TENANT);
        verify(researchRepository).findOne(any(Specification.class));
        verifyNoInteractions(researchMapper);
    }

    @Test
    public void givenAnIdWhenDeleteThenCallRepository() {
        var id = UUID.randomUUID();

        when(researchRepository.deleteByIdAndTenant(id.toString(), TENANT)).thenReturn(1);

        gateway.delete(id, TENANT);

        verify(researchRepository).deleteByIdAndTenant(id.toString(), TENANT);
    }

    @Test
    public void givenAnIdWhenDoesNotDeleteThenThrowsNotFoundException() {
        var id = UUID.randomUUID();

        when(researchRepository.deleteByIdAndTenant(id.toString(), TENANT)).thenReturn(0);

        assertThatThrownBy(() -> gateway.delete(id, TENANT))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(researchRepository).deleteByIdAndTenant(id.toString(), TENANT);
    }

    @Test
    public void givenAResearchWithoutQuestionsWhenGetNextQuestionSequenceThenReturnOne() {
        var id = UUID.randomUUID();

        when(researchRepository.findLastQuestionSequence(id.toString())).thenReturn(Optional.empty());

        var sequence = gateway.getNextQuestionSequence(id);

        assertThat(sequence).isEqualTo(1);
        verify(researchRepository).findLastQuestionSequence(id.toString());
    }

    @Test
    public void givenAResearchWithOptionsWhenGetNextQuestionSequenceThenReturnNextSequence() {
        var id = UUID.randomUUID();

        when(researchRepository.findLastQuestionSequence(id.toString())).thenReturn(Optional.of(1));

        var sequence = gateway.getNextQuestionSequence(id);

        assertThat(sequence).isEqualTo(2);
        verify(researchRepository).findLastQuestionSequence(id.toString());
    }
}
