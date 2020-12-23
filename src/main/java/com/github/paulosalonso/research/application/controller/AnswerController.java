package com.github.paulosalonso.research.application.controller;

import com.github.paulosalonso.research.application.dto.AnswerCriteriaInputDTO;
import com.github.paulosalonso.research.application.dto.AnswerDTO;
import com.github.paulosalonso.research.application.dto.AnswerInputDTO;
import com.github.paulosalonso.research.application.mapper.AnswerDTOMapper;
import com.github.paulosalonso.research.usecase.answer.AnswerCreate;
import com.github.paulosalonso.research.usecase.answer.AnswerRead;
import com.github.paulosalonso.research.usecase.exception.InvalidAnswerException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Api(tags = "Answers")
@RequiredArgsConstructor
@RestController
@RequestMapping("/researches/{researchId}/answers")
public class AnswerController {

    private final AnswerCreate answerCreate;
    private final AnswerRead answerRead;
    private final AnswerDTOMapper mapper;

    @GetMapping
    public List<AnswerDTO> search(@PathVariable UUID researchId, AnswerCriteriaInputDTO answerCriteriaInputDTO) {
        return answerRead.search(mapper.toDomain(researchId, answerCriteriaInputDTO))
                .stream()
                .map(mapper::toDTO)
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerDTO create(@PathVariable UUID researchId, @RequestBody @Valid AnswerInputDTO answerInputDTO) {
        try {
            return mapper.toDTO(answerCreate.create(mapper.toDomain(researchId, answerInputDTO)));
        } catch (InvalidAnswerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
