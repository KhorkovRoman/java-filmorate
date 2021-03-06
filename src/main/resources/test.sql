DELETE FROM FRIENDSHIP;
DELETE FROM FILM_LIKES;
DELETE FROM FILM_GENRES;

DELETE FROM USERS;
ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;

DELETE FROM FILMS;
ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1;

DELETE FROM MPA;
ALTER TABLE MPA ALTER COLUMN MPA_ID RESTART WITH 1;

DELETE FROM GENRES;
ALTER TABLE GENRES ALTER COLUMN GENRE_ID RESTART WITH 1;

DELETE FROM FRIEND_STATUSES;
ALTER TABLE FRIEND_STATUSES ALTER COLUMN FRIEND_STATUS_ID RESTART WITH 1;

CREATE TABLE IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER AUTO_INCREMENT,
    GENRE_NAME CHARACTER VARYING(100) NOT NULL,
    CONSTRAINT GENRES_PK
        PRIMARY KEY (GENRE_ID)
);
CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID   INTEGER AUTO_INCREMENT,
    MPA_NAME CHARACTER VARYING(20) NOT NULL,
    CONSTRAINT MPA_PK
        PRIMARY KEY (MPA_ID)
);
CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID         INTEGER AUTO_INCREMENT,
    FILM_NAME       CHARACTER VARYING(30),
    DESCRIPTION     CHARACTER VARYING(200),
    RELEASE_DATE    DATE,
    DURATION        INTEGER,
    RATE            INTEGER,
    MPA_ID INTEGER,
    CONSTRAINT FILM_PK
        PRIMARY KEY (FILM_ID),
    CONSTRAINT MPA_ID_FK
        FOREIGN KEY (MPA_ID) REFERENCES MPA
);
CREATE TABLE IF NOT EXISTS FILM_GENRES
(
    FILM_ID       INTEGER NOT NULL,
    GENRE_ID      INTEGER NOT NULL,
    CONSTRAINT FILM_GENRES_PK
        PRIMARY KEY (FILM_ID, GENRE_ID),
    CONSTRAINT FILM_GENRES_FILM_ID_FK
        FOREIGN KEY (FILM_ID) REFERENCES FILMS ON DELETE CASCADE,
    CONSTRAINT FILM_GENRES_GENRE_ID_FK
        FOREIGN KEY (GENRE_ID) REFERENCES GENRES ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FRIEND_STATUSES
(
    FRIEND_STATUS_ID    INTEGER AUTO_INCREMENT,
    FRIEND_STATUS_NAME  CHARACTER VARYING(50) NOT NULL,
    CONSTRAINT FRIEND_STATUSES_PK
        PRIMARY KEY (FRIEND_STATUS_ID)
);
CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID   INTEGER AUTO_INCREMENT,
    EMAIL     CHARACTER VARYING(30) NOT NULL,
    LOGIN     CHARACTER VARYING(30) NOT NULL,
    USER_NAME CHARACTER VARYING(30) NOT NULL,
    BIRTHDAY  DATE                  NOT NULL,
    CONSTRAINT USERS_PK
        PRIMARY KEY (USER_ID)
);
CREATE TABLE IF NOT EXISTS FILM_LIKES
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    CONSTRAINT FILM_LIKES_PK
        PRIMARY KEY (FILM_ID, USER_ID),
    CONSTRAINT FILM_LIKES_FILM_ID_FK
        FOREIGN KEY (FILM_ID) REFERENCES FILMS ON DELETE CASCADE,
    CONSTRAINT FILM_LIKES_USER_ID_FK
        FOREIGN KEY (USER_ID) REFERENCES USERS ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS FRIENDSHIP
(
    USER_ID          INTEGER,
    FRIEND_ID        INTEGER,
    FRIEND_STATUS_ID INTEGER,
    CONSTRAINT FRIENDSHIP_PK
        primary key (USER_ID, FRIEND_ID),
    CONSTRAINT FRIENDSHIP_USER_ID_FK
        FOREIGN KEY (USER_ID) REFERENCES USERS ON DELETE CASCADE,
    CONSTRAINT FRIENDSHIP_FRIEND_ID_FK
        FOREIGN KEY (FRIEND_ID) REFERENCES USERS ON DELETE CASCADE,
    CONSTRAINT FRIENDSHIP_FRIEND_STATUS_ID_FK
        FOREIGN KEY (FRIEND_STATUS_ID) REFERENCES FRIEND_STATUSES
);

MERGE INTO MPA (MPA_ID, MPA_NAME)
    VALUES (1, 'G'     ),
           (2, 'PG'    ),
           (3, 'PG-13' ),
           (4, 'R'     ),
           (5, 'NC-17' );

MERGE INTO GENRES (GENRE_ID, GENRE_NAME)
    VALUES (1, '??????????????'        ),
           (2, '??????????'          ),
           (3, '????????????????????'     ),
           (4, '??????????????'        ),
           (5, '????????????????????????????' ),
           (6, '????????????'         );

MERGE INTO FRIEND_STATUSES (FRIEND_STATUS_ID, FRIEND_STATUS_NAME)
    VALUES (1, 'Required' ),
           (2, 'Confirmed' );