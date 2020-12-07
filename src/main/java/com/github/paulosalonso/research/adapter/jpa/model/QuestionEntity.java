package com.github.paulosalonso.research.adapter.jpa.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldNameConstants
@Entity(name = "Question")
@Table(name = "question")
public class QuestionEntity {

    @Id
    private String id;

    @NotBlank
    private String description;

    @NotNull
    private Boolean multiSelect;

    @NotNull
    @ManyToOne(fetch = LAZY)
    private ResearchEntity research;
}
