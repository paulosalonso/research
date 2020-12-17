package com.github.paulosalonso.research.application.controller;

import com.github.paulosalonso.research.application.dto.ResearchCriteriaDTO;
import com.github.paulosalonso.research.application.dto.ResearchDTO;
import com.github.paulosalonso.research.application.dto.ResearchInputDTO;
import com.github.paulosalonso.research.application.mapper.ResearchDTOMapper;
import com.github.paulosalonso.research.usecase.research.ResearchCreate;
import com.github.paulosalonso.research.usecase.research.ResearchDelete;
import com.github.paulosalonso.research.usecase.research.ResearchRead;
import com.github.paulosalonso.research.usecase.research.ResearchUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RestController
@RequestMapping("researches")
public class ResearchController {

    private final ResearchCreate researchCreate;
    private final ResearchRead researchRead;
    private final ResearchUpdate researchUpdate;
    private final ResearchDelete researchDelete;
    private final ResearchDTOMapper mapper;

    @GetMapping("/{id}")
    public ResearchDTO get(@PathVariable UUID id) {
        return mapper.toDTO(researchRead.read(id));
    }

    @GetMapping
    public List<ResearchDTO> search(ResearchCriteriaDTO criteria) {
        return researchRead.search(mapper.toDomain(criteria)).stream()
                .map(mapper::toDTO)
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResearchDTO create(@RequestBody @Valid ResearchInputDTO researchInputDTO) {
        var created = researchCreate.create(mapper.toDomain(researchInputDTO));
        return mapper.toDTO(created);
    }

    @PutMapping("/{id}")
    public ResearchDTO update(@PathVariable UUID id, @RequestBody @Valid ResearchInputDTO researchInputDTO) {
        var research = mapper.toDomain(researchInputDTO).toBuilder()
                .id(id)
                .build();

        var updated = researchUpdate.update(research);
        return mapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        researchDelete.delete(id);
    }

}
