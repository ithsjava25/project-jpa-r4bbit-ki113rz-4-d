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

-- POSTS
-- Demo posts for bulletin app

-- Post 1
INSERT INTO posts (postit_color, subject, message, created_at, user_id)
VALUES ('/Images/PostIt_Blue.jpg', 'What else should I get at the store?',
        'I already put this on my list, please send a fax with additional items you would like.\nCoffee, Milk, PS5 \n// Fiona',
        NOW(), 1);

INSERT INTO post_categories (post_id, category_id)
SELECT p.postId, c.categoryId
FROM posts p, categories c
WHERE p.subject = 'What else should I get at the store?'
  AND c.name IN ('Personal');

-- Post 2

INSERT INTO posts (postit_color, subject, message, created_at, user_id)
VALUES ('/Images/PostIt_LightGreen.jpg', 'HELP',
        'How do you make mustard??\n I’ve been at it forever, boiling it over and over again, but nothing’s happening.',
        '2026-01-04 11:23:00', 5);

INSERT INTO post_categories (post_id, category_id)
SELECT p.postId, c.categoryId
FROM posts p, categories c
WHERE p.subject = 'HELP'
  AND c.name IN ('Important');

-- Post 3
INSERT INTO posts (postit_color, subject, message, created_at, user_id)
VALUES ('/Images/PostIt_Pink.jpg', 'What''s in it for me?',
        'I really gotta know.',
        '2026-01-07 16:05:00', 7);

INSERT INTO post_categories (post_id, category_id)
SELECT p.postId, c.categoryId
FROM posts p, categories c
WHERE p.subject = 'What''s in it for me?'
  AND c.name IN ('Work');

-- Post 4
INSERT INTO posts (postit_color, subject, message, created_at, user_id)
VALUES ('/Images/PostIt_Purple.jpg', 'I swear this time i''m not trolling',
        'Never gonna give you up\nNever gonna let you down\nNever gonna run around and desert you\nNever gonna make you cry\nNever gonna say goodbye\nNever gonna tell a lie and hurt you',
        '2026-01-03 17:38:00', 9);

INSERT INTO post_categories (post_id, category_id)
SELECT p.postId, c.categoryId
FROM posts p, categories c
WHERE p.subject = 'I swear this time i''m not trolling'
  AND c.name IN ('Other');

-- Post 5
INSERT INTO posts (postit_color, subject, message, created_at, user_id)
VALUES ('/Images/PostIt_Yellow.jpg', 'DROP TEST',
        'DROP TABLE secret_oracle_stuff',
        '2026-01-06 01:30:00', 6);

INSERT INTO post_categories (post_id, category_id)
SELECT p.postId, c.categoryId
FROM posts p, categories c
WHERE p.subject = 'DROP TEST'
  AND c.name IN ('School');

-- Post 6
INSERT INTO posts (postit_color, subject, message, created_at, user_id)
VALUES ('/Images/PostIt_Blue.jpg', 'Just a friendly reminder',
        '“Progress matters more than speed.”\n“Even slow steps move you forward.”\n Skalman',
        '2026-01-07 14:30:00', 10);

INSERT INTO post_categories (post_id, category_id)
SELECT p.postId, c.categoryId
FROM posts p, categories c
WHERE p.subject = 'Just a friendly reminder'
  AND c.name IN ('Important' , 'Other');

-- Post 7
INSERT INTO posts (postit_color, subject, message, created_at, user_id)
VALUES ('/Images/PostIt_LightGreen.jpg', 'Lost item',
        'Have someone seen my diamond pickaxe??\nAlso need more redstone if someone could help out.\nThanxx, Steve',
        '2026-01-02 19:30:00', 11);

INSERT INTO post_categories (post_id, category_id)
SELECT p.postId, c.categoryId
FROM posts p, categories c
WHERE p.subject = 'Lost item'
  AND c.name IN ('Important' , 'Personal');

-- Post 8
INSERT INTO posts (postit_color, subject, message, created_at, user_id)
VALUES ('/Images/PostIt_Pink.jpg', 'ROAD WORK AHEAD??',
        'YEA I SURE HOPE IT DOES',
        '2026-01-04 19:36:00', 8);

INSERT INTO post_categories (post_id, category_id)
SELECT p.postId, c.categoryId
FROM posts p, categories c
WHERE p.subject = 'ROAD WORK AHEAD??'
  AND c.name IN ('Other');

-- Post 9
INSERT INTO posts (postit_color, subject, message, created_at, user_id)
VALUES ('/Images/PostIt_Purple.jpg', 'FIONA!',
        'I read your shoppinglist and you are NOT to go buy a PS5 for company money!\nYou also need to stop posting silly posts on here. This is a school project not your playground!\n//Your BOSS',
        NOW(), 3);

INSERT INTO post_categories (post_id, category_id)
SELECT p.postId, c.categoryId
FROM posts p, categories c
WHERE p.subject = 'FIONA!'
  AND c.name IN ('Work' , 'School');
