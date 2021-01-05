package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.AnswerCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchAnswerInputDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchSummaryDTO;
import com.github.paulosalonso.research.adapter.controller.mapper.AnswerDTOMapper;
import com.github.paulosalonso.research.usecase.answer.AnswerCreate;
import com.github.paulosalonso.research.usecase.answer.AnswerRead;
import com.github.paulosalonso.research.usecase.exception.InvalidAnswerException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.UUID;

@Api(tags = "Answers")
@RequiredArgsConstructor
@RestController
@RequestMapping("/researches/{researchId}/answers")
public class AnswerController {

    private final AnswerCreate answerCreate;
    private final AnswerRead answerRead;
    private final AnswerDTOMapper mapper;

    @GetMapping
    public ResearchSummaryDTO search(@PathVariable UUID researchId, AnswerCriteriaDTO answerCriteriaDTO) {
        var result = answerRead.search(mapper.toDomain(researchId, answerCriteriaDTO));
        return mapper.toDTO(result, answerCriteriaDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@PathVariable UUID researchId, @RequestBody @Valid ResearchAnswerInputDTO researchAnswerInputDTO) {
        try {
            var answers = mapper.toDomain(researchId, researchAnswerInputDTO);
            answerCreate.create(researchId, answers);
        } catch (InvalidAnswerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
