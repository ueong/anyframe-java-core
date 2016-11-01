CREATE TABLE SCHEDULING_JOB(
	JOB_NAME VARCHAR(100) NOT NULL, 
	GROUP_NAME VARCHAR(100) NOT NULL, 
	TARGET_CLASS VARCHAR(200) NOT NULL, 
	TARGET_METHOD VARCHAR(100), 
	SCHEDULE VARCHAR(30) NOT NULL, 
	SCHEDULE_TYPE VARCHAR(10) NOT NULL, 
	START_DATE VARCHAR(30) NOT NULL, 
	END_DATE VARCHAR(30), 
	DESCRIPTION VARCHAR(4000), 
	CONSTRAINT PK_SCHEDULING_JOB PRIMARY KEY(JOB_NAME, GROUP_NAME)
);

INSERT INTO SCHEDULING_JOB VALUES('movieUpdateJob','DEFAULT','org.anyframe.plugin.scheduling.job.MovieUpdateJob','','5000','simple','2012-03-01 00:00:00.000',null,'');

commit;