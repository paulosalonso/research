package com.github.paulosalonso.research.application.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuestionDTO {

    private String id;
    private String description;
    private Boolean multiSelect;

}
