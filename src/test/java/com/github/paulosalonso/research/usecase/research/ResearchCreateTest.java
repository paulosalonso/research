package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ResearchCreateTest {

    @InjectMocks
    private ResearchCreate researchCreate;

    @Mock
    private ResearchPort port;

    @Test
    public void givenAResearchWhenCreateThenCallPort() {
        var research = Research.builder().build();

        researchCreate.create(research);

        verify(port).create(research);
    }
}
