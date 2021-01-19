create table answer (
    id bigint not null auto_increment,
    date datetime(6) not null,
    `option_id` varchar(255) not null,
    question_id varchar(255) not null,
    research_id varchar(255) not null,
    primary key (id)
) engine=InnoDB charset=UTF8MB4;

create table `option` (
    id varchar(255) not null,
    sequence int not null,
    description varchar(255) not null,
    notify bit not null,
    question_id varchar(255) not null,
    primary key (id)
) engine=InnoDB charset=UTF8MB4;

create table question (
    id varchar(255) not null,
    sequence int not null,
    description varchar(255) not null,
    multi_select bit not null,
    research_id varchar(255) not null,
    primary key (id)
) engine=InnoDB charset=UTF8MB4;

create table research (
    id varchar(255) not null,
    description varchar(255),
    ends_on datetime(6),
    starts_on datetime(6) not null,
    title varchar(255) not null,
    primary key (id)
) engine=InnoDB charset=UTF8MB4;

alter table answer add constraint FK_answer_option foreign key (`option_id`) references `option` (id);
alter table answer add constraint FK_answer_question foreign key (question_id) references question (id);
alter table answer add constraint FK_answer_research foreign key (research_id) references research (id);
alter table `option` add constraint FK_option_question foreign key (question_id) references question (id);
alter table question add constraint FK_question_research foreign key (research_id) references research (id);
