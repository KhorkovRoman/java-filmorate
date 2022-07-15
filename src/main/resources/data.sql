MERGE INTO MPA (MPA_ID, MPA_NAME)
    VALUES (1, 'G'     ),
           (2, 'PG'    ),
           (3, 'PG-13' ),
           (4, 'R'     ),
           (5, 'NC-17' );

MERGE INTO GENRES (GENRE_ID, GENRE_NAME)
    VALUES (1, 'Комедия'        ),
           (2, 'Драма'          ),
           (3, 'Мультфильм'     ),
           (4, 'Триллер'        ),
           (5, 'Документальный' ),
           (6, 'Боевик'         );

MERGE INTO FRIEND_STATUSES (FRIEND_STATUS_ID, FRIEND_STATUS_NAME)
    VALUES (1, 'Required' ),
           (2, 'Confirmed' );
