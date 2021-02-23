package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.domain.ResearchCriteria;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResearchReadTest {

    private static final String TENANT = "tenant";

    @InjectMocks
    private ResearchRead researchRead;

    @Mock
    private ResearchPort port;

    @Test
    public void givenAUUIDWhenReadWithoutQuestionsThenCallPort() {
        var id = UUID.randomUUID();
        var toRead = Research.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .build();

        when(port.read(id, TENANT)).thenReturn(toRead);

        var result = researchRead.read(id, TENANT, false);

        assertThat(result).isSameAs(toRead);
        verify(port).read(id, TENANT);
    }

    @Test
    public void givenAUUIDWhenReadWithQuestionsThenCallPort() {
        var id = UUID.randomUUID();
        var toRead = Research.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .build();

        when(port.readFetchingQuestions(id, TENANT)).thenReturn(toRead);

        var result = researchRead.read(id, TENANT, true);

        assertThat(result).isSameAs(toRead);
        verify(port).readFetchingQuestions(id, TENANT);
    }

    @Test
    public void givenAResearchCriteriaWhenSearchThenCallPort() {
        var criteria = ResearchCriteria.builder().build();
        var toSearch = List.of(Research.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .build());

        when(port.search(criteria)).thenReturn(toSearch);

        var result = researchRead.search(criteria);

        assertThat(result).isSameAs(toSearch);
        verify(port).search(criteria);
    }
}
