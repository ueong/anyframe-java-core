CREATE TABLE IDS(
	TABLE_NAME VARCHAR(16) NOT NULL PRIMARY KEY,
	NEXT_ID DECIMAL(30) NOT NULL);
	
CREATE TABLE MY_IDS(
	MY_KEY VARCHAR(16) NOT NULL PRIMARY KEY,
	MY_ID DECIMAL(30) NOT NULL);
	
INSERT INTO IDS VALUES('IDGEN_MOVIE',1);
INSERT INTO MY_IDS VALUES('test', 1);

commit;

