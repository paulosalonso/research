package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ResearchCreateTest {

    @InjectMocks
    private ResearchCreate researchCreate;

    @Mock
    private ResearchPort port;

    @Test
    public void givenAResearchWhenCreateThenCallPort() {
        var toSave = Research.builder()
                .description("description")
                .startsOn(LocalDateTime.now())
                .build();

        researchCreate.create(toSave);

        ArgumentCaptor<Research> researchCaptor = ArgumentCaptor.forClass(Research.class);
        verify(port).create(researchCaptor.capture());

        var saved = researchCaptor.getValue();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescription()).isEqualTo(toSave.getDescription());
        assertThat(saved.getStartsOn()).isEqualTo(toSave.getStartsOn());
        assertThat(saved.getEndsOn()).isEqualTo(toSave.getEndsOn());
        assertThat(saved.getQuestions()).isEqualTo(toSave.getQuestions());
    }
}
