drop table if exists voucher;
create table voucher (
    id int identity,
    code varchar(6),
    veryImportant varchar,
    alsoImportant varchar,
    createdBy varchar,
    createdAt timestamp,
    updatedBy varchar,
    updatedAt timestamp
);
