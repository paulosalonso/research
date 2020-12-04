package com.github.paulosalonso.research.usecase.option;

import com.github.paulosalonso.research.usecase.port.OptionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OptionDeleteTest {

    @InjectMocks
    private OptionDelete optionDelete;

    @Mock
    private OptionPort port;

    @Test
    public void givenAQuestionIdAndAnOptionIdWhenDeleteThenCallPort() {
        var questionId = UUID.randomUUID();
        var optionId = UUID.randomUUID();

        optionDelete.delete(questionId, optionId);

        verify(port).delete(questionId, optionId);
    }
}
