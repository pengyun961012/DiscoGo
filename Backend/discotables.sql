CREATE TABLE Users (
    u_id INTEGER,
    username VARCHAR(100), 
    token INTEGER,
    img_id INTEGER,
    level INTEGER,
    PRIMARY KEY (u_id));

CREATE TABLE Friends (
    u1_id INTEGER,
    u2_id INTEGER,
    PRIMARY KEY (u1_id, u2_id),
    FOREIGN KEY (u1_id) REFERENCES users(u_id),
    FOREIGN KEY (u2_id) REFERENCES users(u_id));

-- Table:
-- u_id, sing_time, score, link, song_name
CREATE TABLE Songs (
    u_id INTEGER,
    sing_time TIMESTAMP,
    score INTEGER,
    link VARCHAR(100),
    song_name VARCHAR(100), 
    PRIMARY KEY (u_id, sing_time));

INSERT INTO Users (u_id, username, token, img_id, level) VALUES (1, 'llw', 100, 10, 1);
INSERT INTO Users (u_id, username, token, img_id, level) VALUES (2, 'jhe', 200, 5, 1);
INSERT INTO Users (u_id, username, token, img_id, level) VALUES (3, 'cmm', 300, 8, 1);
INSERT INTO Users (u_id, username, token, img_id, level) VALUES (4, 'qyao', 400, 9, 1);
INSERT INTO Users (u_id, username, token, img_id, level) VALUES (5, 'peterz', 500, 15, 1);
INSERT INTO Users (u_id, username, token, img_id, level) VALUES (6, 'fhu', 300, 11, 1);
INSERT INTO Users (u_id, username, token, img_id, level) VALUES (7, 'lurz', 800, 12, 1);

INSERT INTO Friends (u1_id, u2_id) VALUES (1, 2);
INSERT INTO Friends (u1_id, u2_id) VALUES (2, 3);
INSERT INTO Friends (u1_id, u2_id) VALUES (3, 4);
INSERT INTO Friends (u1_id, u2_id) VALUES (5, 6);
INSERT INTO Friends (u1_id, u2_id) VALUES (3, 7);
INSERT INTO Friends (u1_id, u2_id) VALUES (2, 5);

INSERT INTO Songs (u_id, sing_time, score, link, song_name) VALUES 
(1, '2019-01-19 20:43:56', 2300, '', 'alphabet');
INSERT INTO Songs (u_id, sing_time, score, link, song_name) VALUES 
(1, '2019-01-15 20:42:56', 2500, '', 'alphabet');
INSERT INTO Songs (u_id, sing_time, score, link, song_name) VALUES 
(1, '2019-01-19 12:43:53', 2312, '', 'alphabet');
INSERT INTO Songs (u_id, sing_time, score, link, song_name) VALUES 
(1, '2019-01-20 16:43:34', 2000, '', 'alphabet');
INSERT INTO Songs (u_id, sing_time, score, link, song_name) VALUES 
(2, '2019-01-15 22:42:34', 2300, '', 'alphabet');
INSERT INTO Songs (u_id, sing_time, score, link, song_name) VALUES 
(2, '2019-01-19 10:23:36', 2300, '', 'alphabet');