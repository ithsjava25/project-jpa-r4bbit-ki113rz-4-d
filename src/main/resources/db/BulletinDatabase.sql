create database if not exists bulletin_demo;
use bulletin_demo;

create table if not exists user_account (
    userId bigint auto_increment primary key,
    first_name varchar(255) not null ,
    last_name varchar(255) not null ,
    username varchar(255) not null unique ,
    password varchar(255) not null
);

create table if not exists categories (
    categoryId bigint auto_increment primary key ,
    name varchar(255) not null unique
);

create table if not exists posts (
    postId bigint auto_increment primary key ,
    postit_color varchar(255) not null ,
    subject varchar(255) not null ,
    message varchar(255) not null ,
    created_at datetime not null ,
    user_id bigint not null ,
    foreign key (user_id) references user_account(userId) on delete cascade
);

create table if not exists profile (
    id bigint auto_increment primary key,
    bio text,
    user_id bigint not null unique ,
    foreign key (user_id) references user_account(userId) on delete cascade
);
