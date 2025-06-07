--liquibase formatted sql
--changeset tregubchenko:initial

create table if not exists orders_history (
     id          UUID,
     name        varchar,
     price       integer,
     status      varchar,
     date        date,
     version     bigint      default 0,
     constraint pk_orders_history primary key (id)
);

--rollback drop table if not exists orders_history cascade;