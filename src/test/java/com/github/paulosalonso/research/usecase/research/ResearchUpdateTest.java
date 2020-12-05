package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResearchUpdateTest {

    @InjectMocks
    private ResearchUpdate researchUpdate;

    @Mock
    private ResearchPort port;

    @Test
    public void givenAResearchWhenUpdateThenCallPort() {
        var toUpdate = Research.builder()
                .description("description")
                .startsOn(OffsetDateTime.now())
                .build();

        var updated = Research.builder()
                .description("description")
                .startsOn(OffsetDateTime.now())
                .build();

        when(port.update(toUpdate)).thenReturn(updated);

        var result = researchUpdate.update(toUpdate);

        assertThat(result).isSameAs(updated);
        verify(port).update(toUpdate);
    }
}
