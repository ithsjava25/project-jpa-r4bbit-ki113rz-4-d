drop database bulletin_demo;

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

INSERT IGNORE INTO categories (name)
VALUES
    ('School'),
    ('Work'),
    ('Personal'),
    ('Important'),
    ('Other');


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

create table if not exists post_categories (
    post_id bigint not null ,
    category_id bigint not null ,
    PRIMARY KEY (post_id, category_id) ,
    foreign key (post_id) references posts(postId) on DELETE cascade ,
    foreign key (category_id) references categories(categoryId) on DELETE cascade
);

insert ignore into user_account (first_name, last_name, username, password)
VALUES
    ('Fiona', 'Fribe', 'FiffenBiffen' , '123123'),
    ('Sandra' , 'Nelj' , 'Sandra' , 'sandra'),
    ('Daniel' , 'Mart' , 'Daniel' , 'daniel'),
    ('Edvin' , 'Karl' , 'Edvin' , 'edvin'),
    ('Ulf' , 'Bilt' , 'Cool_Ulf' , 'ulf'),
    ('Martin' , 'Blom' , 'JavaKiiingMartin' , 'martin'),
    ('Amy' , 'Deasi' , 'DiamantAmy' , 'amy'),
    ('Drew' , 'Good' , 'VineLord' , 'drew'),
    ('Rick' , 'Astley' , 'rickroller' , 'rick'),
    ('Skal' , 'Man' , 'Skalman', 'skalman'),
    ('Steve' , 'Minecraft' , 'CreeperDestroyer' , 'steve')
;
INSERT INTO posts (postit_color, subject, message, created_at, user_id)
VALUES ('/Images/PostIt_Blue.jpg', 'What else should I get at the store?',
        'I already put this on my list, please send a fax with additional items you would like.\nCoffee, Milk, PS5 \n// Fiona',
        NOW(), 1);

INSERT INTO post_categories (post_id, category_id)
SELECT p.postId, c.categoryId
FROM posts p, categories c
WHERE p.subject = 'What else should I get at the store?'
  AND c.name IN ('Personal');
