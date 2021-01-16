package com.github.paulosalonso.research.usecase.option;

import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.domain.OptionCriteria;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OptionReadTest {

    @InjectMocks
    private OptionRead optionRead;

    @Mock
    private OptionPort port;

    @Test
    public void givenAQuestionIdAndAnOptionIdIdWhenReadThenCallPort() {
        var questionId = UUID.randomUUID();
        var optionId = UUID.randomUUID();
        var toRead = Option.builder()
                .description("description")
                .build();

        when(port.read(questionId, optionId)).thenReturn(toRead);

        var result = optionRead.read(questionId, optionId);

        assertThat(result).isSameAs(toRead);
        verify(port).read(questionId, optionId);
    }

    @Test
    public void givenAQuestionIdAndAnOptionCriteriaWhenSearchThenCallPort() {
        var id = UUID.randomUUID();
        var criteria = OptionCriteria.builder().build();
        var toSearch = List.of(Option.builder()
                .description("description")
                .build());

        when(port.search(id, criteria)).thenReturn(toSearch);

        var result = optionRead.search(id, criteria);

        assertThat(result).isSameAs(toSearch);
        verify(port).search(id, criteria);
    }
}
