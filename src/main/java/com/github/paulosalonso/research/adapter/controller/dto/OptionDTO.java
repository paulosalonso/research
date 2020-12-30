package com.github.paulosalonso.research.adapter.controller.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OptionDTO {

    private String id;
    private Integer sequence;
    private String description;
}
