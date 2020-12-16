package com.github.paulosalonso.research.application.controller;

import com.github.paulosalonso.research.application.dto.QuestionCriteriaDTO;
import com.github.paulosalonso.research.application.dto.QuestionDTO;
import com.github.paulosalonso.research.application.dto.QuestionInputDTO;
import com.github.paulosalonso.research.application.mapper.QuestionDTOMapper;
import com.github.paulosalonso.research.usecase.question.QuestionCreate;
import com.github.paulosalonso.research.usecase.question.QuestionDelete;
import com.github.paulosalonso.research.usecase.question.QuestionRead;
import com.github.paulosalonso.research.usecase.question.QuestionUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

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
    public QuestionDTO get(@PathVariable UUID researchId, @PathVariable UUID questionId) {
        return mapper.toDTO(questionRead.read(researchId, questionId));
    }

    @GetMapping
    public List<QuestionDTO> search(@PathVariable UUID researchId, QuestionCriteriaDTO criteria) {
        return questionRead.search(researchId, mapper.toDomain(criteria)).stream()
                .map(mapper::toDTO)
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDTO create(@PathVariable UUID researchId, @RequestBody @Valid QuestionInputDTO questionInputDTO) {
        var created = questionCreate.create(researchId, mapper.toDomain(questionInputDTO));
        return mapper.toDTO(created);
    }

    @PutMapping("/{questionId}")
    public QuestionDTO update(@PathVariable UUID researchId, @PathVariable UUID questionId,
                              @RequestBody @Valid QuestionInputDTO questionInputDTO) {

        var question = mapper.toDomain(questionInputDTO).toBuilder()
                .id(questionId)
                .build();

        var updated = questionUpdate.update(researchId, question);
        return mapper.toDTO(updated);
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID researchId, @PathVariable UUID questionId) {
        questionDelete.delete(researchId, questionId);
    }

}
