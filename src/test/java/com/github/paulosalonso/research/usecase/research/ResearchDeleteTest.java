package com.github.paulosalonso.research.usecase.research;

import com.github.paulosalonso.research.usecase.port.ResearchPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ResearchDeleteTest {

    private static final String TENANT = "tenant";

    @InjectMocks
    private ResearchDelete researchDelete;

    @Mock
    private ResearchPort port;

    @Test
    public void givenAResearchWhenCreateThenCallPort() {
        var id = UUID.randomUUID();

        researchDelete.delete(id, TENANT);

        verify(port).delete(id, TENANT);
    }
}
