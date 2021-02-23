package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.ResearchCriteriaDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchInputDTO;
import com.github.paulosalonso.research.adapter.controller.mapper.ResearchDTOMapper;
import com.github.paulosalonso.research.application.security.SecurityInfo;
import com.github.paulosalonso.research.usecase.research.ResearchCreate;
import com.github.paulosalonso.research.usecase.research.ResearchDelete;
import com.github.paulosalonso.research.usecase.research.ResearchRead;
import com.github.paulosalonso.research.usecase.research.ResearchUpdate;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.github.paulosalonso.research.application.security.SecurityExpressions.IS_ADMIN;
import static java.util.stream.Collectors.toList;

@Api(tags = "Researches")
@RequiredArgsConstructor
@RestController
@RequestMapping("researches")
public class ResearchController {

    private final ResearchCreate researchCreate;
    private final ResearchRead researchRead;
    private final ResearchUpdate researchUpdate;
    private final ResearchDelete researchDelete;
    private final ResearchDTOMapper mapper;
    private final SecurityInfo securityInfo;

    @GetMapping("/{id}")
    public ResearchDTO get(@PathVariable UUID id, @RequestParam(required = false) boolean fillQuestions) {
        return mapper.toDTO(researchRead.read(id, securityInfo.getTenant(), fillQuestions), fillQuestions);
    }

    @GetMapping
    public List<ResearchDTO> search(ResearchCriteriaDTO criteria) {
        return researchRead.search(mapper.toDomain(criteria, securityInfo.getTenant())).stream()
                .map(research -> mapper.toDTO(research, false))
                .collect(toList());
    }

    @PreAuthorize(IS_ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResearchDTO create(@RequestBody @Valid ResearchInputDTO researchInputDTO) {
        var created = researchCreate.create(mapper.toDomain(researchInputDTO, securityInfo.getTenant()));
        return mapper.toDTO(created, false);
    }

    @PreAuthorize(IS_ADMIN)
    @PutMapping("/{id}")
    public ResearchDTO update(@PathVariable UUID id, @RequestBody @Valid ResearchInputDTO researchInputDTO) {
        var research = mapper.toDomain(researchInputDTO, securityInfo.getTenant()).toBuilder()
                .id(id)
                .build();

        var updated = researchUpdate.update(research);
        return mapper.toDTO(updated, false);
    }

    @PreAuthorize(IS_ADMIN)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        researchDelete.delete(id, securityInfo.getTenant());
    }

}
