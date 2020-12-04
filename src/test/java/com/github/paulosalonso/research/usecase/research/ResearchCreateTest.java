package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResearchCreateTest {

    @InjectMocks
    private ResearchCreate researchCreate;

    @Mock
    private ResearchPort port;

    @Test
    public void givenAResearchWhenCreateThenCallPort() {
        var toSave = Research.builder().build();
        var saved = Research.builder().build();

        when(port.create(toSave)).thenReturn(saved);

        var result = researchCreate.create(toSave);

        assertThat(result).isSameAs(saved);
        verify(port).create(toSave);
    }
}
