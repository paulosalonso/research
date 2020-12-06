package com.github.paulosalonso.research.adapter.jpa.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldNameConstants
@Entity(name = "Research")
@Table(name = "research")
public class ResearchEntity {

    @Id
    private String id;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private OffsetDateTime startsOn;

    private OffsetDateTime endsOn;
}
