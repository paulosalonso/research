package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.QuestionCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.QuestionDTO;
import com.github.paulosalonso.research.adapter.controller.dto.QuestionInputDTO;
import com.github.paulosalonso.research.adapter.controller.mapper.QuestionDTOMapper;
import com.github.paulosalonso.research.usecase.question.QuestionCreate;
import com.github.paulosalonso.research.usecase.question.QuestionDelete;
import com.github.paulosalonso.research.usecase.question.QuestionRead;
import com.github.paulosalonso.research.usecase.question.QuestionUpdate;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Api(tags = "Questions")
@RequiredArgsConstructor
@RestController
@RequestMapping("/researches/{researchId}/questions")
public class QuestionController {

    private final QuestionCreate questionCreate;
    private final QuestionRead questionRead;
    private final QuestionUpdate questionUpdate;
    private final QuestionDelete questionDelete;
    private final QuestionDTOMapper mapper;

    @GetMapping("/{questionId}")
    public QuestionDTO get(@PathVariable UUID researchId,
            @PathVariable UUID questionId, @RequestParam(required = false) boolean fillOptions) {
        return mapper.toDTO(questionRead.read(researchId, questionId, fillOptions), fillOptions);
    }

    @GetMapping
    public List<QuestionDTO> search(@PathVariable UUID researchId, QuestionCriteriaDTO criteria) {
        return questionRead.search(researchId, mapper.toDomain(criteria)).stream()
                .map(question -> mapper.toDTO(question, false))
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDTO create(@PathVariable UUID researchId, @RequestBody @Valid QuestionInputDTO questionInputDTO) {
        var created = questionCreate.create(researchId, mapper.toDomain(questionInputDTO));
        return mapper.toDTO(created, false);
    }

    @PutMapping("/{questionId}")
    public QuestionDTO update(@PathVariable UUID researchId, @PathVariable UUID questionId,
                              @RequestBody @Valid QuestionInputDTO questionInputDTO) {

        var question = mapper.toDomain(questionInputDTO).toBuilder()
                .id(questionId)
                .build();

        var updated = questionUpdate.update(researchId, question);
        return mapper.toDTO(updated, false);
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID researchId, @PathVariable UUID questionId) {
        questionDelete.delete(researchId, questionId);
    }

}
