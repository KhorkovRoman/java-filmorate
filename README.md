# java-filmorate
Template repository for Filmorate project.

![Screenshot](Filmorate_DB_scheme.png)

Examples of SQL:

create table FILM_LIKES(

    FILM_LIKES_ID INTEGER auto_increment,
    FILM_ID       INTEGER,
    USER_ID       INTEGER,
    constraint FILM_LIKES_PK
    primary key (FILM_LIKES_ID),
    constraint FILM_LIKES_FILM_ID_FK
    foreign key (FILM_ID) references FILMS,
    constraint FILM_LIKES_USER_ID_FK
    foreign key (USER_ID) references USERS
);

create table FILMS(

    FILM_ID        INTEGER auto_increment,
    NAME           CHARACTER VARYING(30) not null,
    DESCRIPTION    CHARACTER VARYING(200),
    "releaseDate"  DATE,
    DURATION       INTEGER,
    RATE           INTEGER,
    RATING_FILM_ID INTEGER,
    constraint FILMS_PK
    primary key (FILM_ID),
    constraint RATING_FILM_ID_FK
    foreign key (RATING_FILM_ID) references RATING_FILM
);