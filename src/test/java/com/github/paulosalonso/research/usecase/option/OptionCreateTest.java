package com.github.paulosalonso.research.usecase.option;

import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OptionCreateTest {

    @InjectMocks
    private OptionCreate optionCreate;

    @Mock
    private OptionPort port;

    @Test
    public void givenAQuestionIdAndAnOptionWhenCreateThenCallPort() {
        var id = UUID.randomUUID();
        var toSave = Option.builder().build();
        var saved = Option.builder().build();

        when(port.create(id, toSave)).thenReturn(saved);

        var result = optionCreate.create(id, toSave);

        assertThat(result).isSameAs(saved);
        verify(port).create(id, toSave);
    }
}
