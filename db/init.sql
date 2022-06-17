create table account
(
    id         bigserial
        primary key,
    created_at timestamp   not null,
    username   varchar(64) not null
        constraint account_username_unique
            unique
);

create table account_data
(
    id         bigserial
        primary key,
    created_at timestamp    not null,
    name       varchar(128) not null,
    account_id bigint       not null
        constraint fk_account_data_account_id__id
            references account
            on update restrict on delete restrict
);

create table company
(
    id         bigserial
        primary key,
    created_at timestamp   not null,
    codename   varchar(64) not null
        constraint company_codename_unique
            unique
);

create table company_placement
(
    id         bigserial
        primary key,
    created_at timestamp   not null,
    codename   varchar(64) not null
        constraint company_placement_codename_unique
            unique,
    company_id bigint      not null
        constraint fk_company_placement_company_id__id
            references company
            on update restrict on delete restrict
);

create table company_placement_data
(
    id           bigserial
        primary key,
    created_at   timestamp    not null,
    placement_id bigint       not null
        constraint fk_company_placement_data_placement_id__id
            references company_placement
            on update restrict on delete cascade,
    title        varchar(128) not null
);

create table company_member
(
    id         bigserial
        primary key,
    created_at timestamp not null,
    company_id bigint    not null
        constraint fk_company_member_company_id__id
            references company
            on update restrict on delete cascade,
    account_id bigint    not null
        constraint fk_company_member_account_id__id
            references account
            on update restrict on delete restrict
);

create table company_placement_member
(
    id           bigserial
        primary key,
    created_at   timestamp not null,
    placement_id bigint    not null
        constraint fk_company_placement_member_placement_id__id
            references company_placement
            on update restrict on delete cascade,
    member_id    bigint    not null
        constraint fk_company_placement_member_member_id__id
            references company_member
            on update restrict on delete restrict,
    account_id   bigint    not null
        constraint fk_company_placement_member_account_id__id
            references account
            on update restrict on delete restrict
);

create table company_data
(
    id         bigserial
        primary key,
    created_at timestamp    not null,
    company_id bigint       not null
        constraint fk_company_data_company_id__id
            references company
            on update restrict on delete cascade,
    title      varchar(128) not null
);

create table company_owner
(
    id         bigserial
        primary key,
    created_at timestamp not null,
    company_id bigint    not null
        constraint fk_company_owner_company_id__id
            references company
            on update restrict on delete cascade,
    account_id bigint    not null
        constraint fk_company_owner_account_id__id
            references account
            on update restrict on delete restrict,
    member_id  bigint    not null
        constraint fk_company_owner_member_id__id
            references company_member
            on update restrict on delete restrict
);

create table account_password
(
    id            bigserial
        primary key,
    created_at    timestamp   not null,
    account_id    bigint      not null
        constraint fk_account_password_account_id__id
            references account
            on update restrict on delete restrict,
    password_hash varchar(70) not null
);

create table account_session
(
    id         bigserial
        primary key,
    created_at timestamp not null,
    account_id bigint    not null
        constraint fk_account_session_account_id__id
            references account
            on update restrict on delete restrict
);

create table account_refresh_token_table
(
    id         bigserial
        primary key,
    created_at timestamp not null,
    account_id bigint    not null
        constraint fk_account_refresh_token_table_account_id__id
            references account
            on update restrict on delete restrict
);

create table company_employee
(
    id         bigserial
        primary key,
    created_at timestamp not null,
    company_id bigint    not null
        constraint fk_company_employee_company_id__id
            references company
            on update restrict on delete restrict
);

create table company_placement_employee
(
    id           bigserial
        primary key,
    created_at   timestamp not null,
    employee_id  bigint    not null
        constraint fk_company_placement_employee_employee_id__id
            references company_employee
            on update restrict on delete restrict,
    placement_id bigint    not null
        constraint fk_company_placement_employee_placement_id__id
            references company_placement
            on update restrict on delete restrict
);

create table company_employee_data
(
    id          bigserial
        primary key,
    created_at  timestamp   not null,
    employee_id bigint      not null
        constraint fk_company_employee_data_employee_id__id
            references company_employee
            on update restrict on delete restrict,
    name        varchar(64) not null
);


