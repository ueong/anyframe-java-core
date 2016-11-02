CREATE TABLE VO_GENRE(
	GENRE_ID VARCHAR(5) NOT NULL,
	NAME VARCHAR(50) NOT NULL,
	CONSTRAINT PK_VO_GENRE PRIMARY KEY(GENRE_ID));

CREATE TABLE VO_MOVIE(
	MOVIE_ID VARCHAR(8) NOT NULL,
	GENRE_ID VARCHAR(5) NOT NULL,
	TITLE VARCHAR(50) NOT NULL,
	DIRECTOR VARCHAR(50) NOT NULL,
	ACTORS VARCHAR(100) NOT NULL,
	RUNTIME NUMERIC(3),
	RELEASE_DATE DATE default CURRENT DATE,
	TICKET_PRICE NUMERIC(6,2),
	SIMPLE_UPLOAD_FILE_PATH VARCHAR(1000),
	NOW_PLAYING CHAR(1),
	CONSTRAINT PK_VO_MOVIE PRIMARY KEY(MOVIE_ID),
	CONSTRAINT FK_VO_MOVIE FOREIGN KEY(GENRE_ID) REFERENCES VO_GENRE(GENRE_ID));
	
CREATE TABLE VO_IDS(
	TABLE_NAME VARCHAR(16) NOT NULL PRIMARY KEY,
	NEXT_ID DECIMAL(30) NOT NULL);
	
INSERT INTO VO_GENRE VALUES('GR-01','Action');
INSERT INTO VO_GENRE VALUES('GR-02','Adventure');
INSERT INTO VO_GENRE VALUES('GR-03','Animation');
INSERT INTO VO_GENRE VALUES('GR-04','Comedy');
INSERT INTO VO_GENRE VALUES('GR-05','Crime');
INSERT INTO VO_GENRE VALUES('GR-06','Drama');
INSERT INTO VO_GENRE VALUES('GR-07','Fantasy');
INSERT INTO VO_GENRE VALUES('GR-08','Romance');
INSERT INTO VO_GENRE VALUES('GR-09','Sci-Fi');
INSERT INTO VO_GENRE VALUES('GR-10','Thriller');

INSERT INTO VO_MOVIE VALUES('MV-00001','GR-02','Alice in Wonderland','Tim Burton','Johnny Depp',110,DATE('2010-03-04'),8000,'sample/images/posters/aliceinwonderland.jpg','Y');
INSERT INTO VO_MOVIE VALUES('MV-00002','GR-09','Avatar','James Cameron','Sigourney Weaver',100,DATE('2010-02-16'),7000,'sample/images/posters/avatar.jpg','Y');
INSERT INTO VO_MOVIE VALUES('MV-00003','GR-01','Green Zone','Paul Greengrass','Yigal Naor',90,DATE('2010-03-25'),7000,'sample/images/posters/greenzone.jpg','Y');
INSERT INTO VO_MOVIE VALUES('MV-00004','GR-06','Remember Me','Allen Coulter','Caitlyn Rund',115,DATE('2010-03-12'),8000,'sample/images/posters/rememberme.jpg','Y');
INSERT INTO VO_MOVIE VALUES('MV-00005','GR-04','She is Out of My League','Jim Field Smith','Jay Baruchel',118,DATE('2010-03-12'),7500,'sample/images/posters/shesoutof.jpg','Y');
INSERT INTO VO_MOVIE VALUES('MV-00006','GR-05','Shutter Island','Martin Scorsese','Leonardo DiCaprio',95,DATE('2010-03-18'),8000,'sample/images/posters/shutterisland.jpg','Y');

INSERT INTO VO_IDS VALUES('VO_MOVIE',7);

commit;

