--liquibase formatted sql
--changeset tregubchenko:initial

create table if not exists orders (
    id          UUID,
    name        varchar,
    price       integer,
    status      varchar,
    date        date,
    version     bigint      default 0,
    constraint pk_orders primary key (id)
);

--rollback drop table if not exists orders cascade;