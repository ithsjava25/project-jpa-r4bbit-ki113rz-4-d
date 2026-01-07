create database if not exists bulletin_demo;
use bulletin_demo;

create table if not exists user_account (
    userId bigint auto_increment primary key,
    first_name varchar(255) not null ,
    last_name varchar(255) not null ,
    username varchar(255) not null unique ,
    password varchar(255) not null
);
