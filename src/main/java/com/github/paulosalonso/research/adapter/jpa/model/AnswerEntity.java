package com.github.paulosalonso.research.adapter.jpa.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldNameConstants
@Entity(name = "Answer")
@Table(name = "answer")
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private OffsetDateTime date;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ResearchEntity research;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionEntity question;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`option_id`") // Column name escaped for MySQL compatibility, because "option" table name is quoted because is a reserved word
    private OptionEntity option;
}
