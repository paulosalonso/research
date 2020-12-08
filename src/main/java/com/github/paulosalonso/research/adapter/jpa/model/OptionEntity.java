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
@Entity(name = "Option")
@Table(name = "option")
public class OptionEntity {

    @Id
    private String id;

    @NotBlank
    private String description;

    @NotNull
    @ManyToOne(fetch = LAZY)
    private QuestionEntity question;
}
