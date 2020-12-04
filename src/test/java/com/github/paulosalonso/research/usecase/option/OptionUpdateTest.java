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
public class OptionUpdateTest {

    @InjectMocks
    private OptionUpdate optionUpdate;

    @Mock
    private OptionPort port;

    @Test
    public void givenAQuestionIdAndAnOptionWhenUpdateThenCallPort() {
        var id = UUID.randomUUID();
        var toUpdate = Option.builder().build();
        var updated = Option.builder().build();

        when(port.update(id, toUpdate)).thenReturn(updated);

        var result = optionUpdate.update(id, toUpdate);

        assertThat(result).isSameAs(updated);
        verify(port).update(id, toUpdate);
    }
}
