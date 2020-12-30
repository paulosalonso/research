package com.github.paulosalonso.research.adapter.controller.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuestionDTO {

    private String id;
    private Integer sequence;
    private String description;
    private Boolean multiSelect;

}
