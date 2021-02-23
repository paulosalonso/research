package com.github.paulosalonso.research.adapter.jpa.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private String tenant;

    @NotNull
    private Integer sequence;

    @NotBlank
    private String description;

    @NotNull
    private Boolean multiSelect;

    @NotNull
    @ManyToOne(fetch = LAZY)
    private ResearchEntity research;

    @OrderBy("sequence")
    @OneToMany(mappedBy = "question")
    private List<OptionEntity> options;
}
