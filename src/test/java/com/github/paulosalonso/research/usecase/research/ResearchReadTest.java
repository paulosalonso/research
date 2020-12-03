package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.domain.ResearchCriteria;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ResearchReadTest {

    @InjectMocks
    private ResearchRead researchRead;

    @Mock
    private ResearchPort port;

    @Test
    public void givenAUUIDWhenReadThenCallPort() {
        var id = UUID.randomUUID();

        researchRead.read(id);

        verify(port).read(id);
    }

    @Test
    public void givenAResearchCriteriaWhenSearchThenCallPort() {
        var criteria = ResearchCriteria.builder().build();

        researchRead.search(criteria);

        verify(port).search(criteria);
    }
}
