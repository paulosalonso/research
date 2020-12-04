package com.github.paulosalonso.research.usecase.option;

import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OptionCreateTest {

    @InjectMocks
    private OptionCreate optionCreate;

    @Mock
    private OptionPort port;

    @Test
    public void givenAQuestionIdAndAnOptionWhenCreateThenCallPort() {
        var id = UUID.randomUUID();

        var toSave = Option.builder()
                .description("description")
                .build();

        optionCreate.create(id, toSave);

        ArgumentCaptor<Option> optionCaptor = ArgumentCaptor.forClass(Option.class);
        verify(port).create(eq(id), optionCaptor.capture());

        var saved = optionCaptor.getValue();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescription()).isEqualTo(toSave.getDescription());
    }
}
