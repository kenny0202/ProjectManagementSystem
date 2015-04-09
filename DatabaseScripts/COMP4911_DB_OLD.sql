DROP DATABASE IF EXISTS COMP4911_DB;
CREATE DATABASE COMP4911_DB;

GRANT ALL PRIVILEGES ON COMP4911_DB.* TO COMP4911_DB@localhost IDENTIFIED BY 'COMP4911_DB' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON COMP4911_DB.* TO COMP4911_DB@"%" IDENTIFIED BY 'COMP4911_DB' WITH GRANT OPTION;
DROP TABLE IF EXISTS COMP4911_DB.EMPLOYEE;
DROP TABLE IF EXISTS COMP4911_DB.CREDENTIALS;
DROP TABLE IF EXISTS COMP4911_DB.PROJECTS;
DROP TABLE IF EXISTS COMP4911_DB.PACKAGES;
DROP TABLE IF EXISTS COMP4911_DB.EMPLOYEE_PROJECT_ASSIGNMENT;
DROP TABLE IF EXISTS COMP4911_DB.LABOUR_GRADE_CHARGES;
DROP TABLE IF EXISTS COMP4911_DB.STATUS_REPORT;
DROP TABLE IF EXISTS COMP4911_DB.timesheetRow;
DROP TABLE IF EXISTS COMP4911_DB.timesheet;


-- CREATE TABLE EMPLOYEE
CREATE TABLE COMP4911_DB.EMPLOYEE
(
	EMPLOYEE_ID VARCHAR(50),
	LABOUR_GRADE_ID VARCHAR(20),
	ROLE VARCHAR(20), 
	SUPERVISOR_ID VARCHAR(50), 
	FIRST_NAME VARCHAR(50),
	LAST_NAME VARCHAR(50),
	USER_NAME VARCHAR(50),
	ADDRESS VARCHAR(50),
	PROVINCE VARCHAR(50),
	COUNTRY VARCHAR(50),
	POSTAL_CODE VARCHAR(50),
	HOME_NUMBER VARCHAR(50),
	CELL_NUMBER VARCHAR(50),
	EMAIL_ADDRESS VARCHAR(50),
	SICK_LEAVE_TIME_BALANCE DOUBLE,
	VACATION_TIME_BALANCE DOUBLE,
	PRIMARY KEY(EMPLOYEE_ID)
 );
 
-- CREATE TABLE CREDENTIALS
CREATE TABLE COMP4911_DB.CREDENTIALS
(
	USER_NAME VARCHAR(50) NOT NULL UNIQUE, 
	PASSWORD VARCHAR(50) NOT NULL, 
	PRIMARY KEY(USER_NAME)
);

-- CREATE TABLE PROJECTS
CREATE TABLE COMP4911_DB.PROJECTS
(
	PROJECT_ID VARCHAR(20) UNIQUE,
	PACKAGE_ID VARCHAR(20),
	PROJECT_MANAGER_USERNAME VARCHAR(50),
	DESCRIPTION VARCHAR(200),
	CLIENT_NAME VARCHAR(50),
	START_DATE DATE,
	END_DATE DATE,
	MARKUP_RATE DOUBLE,
	PRIMARY KEY(PROJECT_ID)
);

-- CREATE TABLE PACKAGES
CREATE TABLE COMP4911_DB.PACKAGES
(
	PACKAGE_ID VARCHAR(20),
	PROJECT_ID VARCHAR(50),
	ENGINEER_USERNAME VARCHAR(50),
	STATUS VARCHAR(20),
	DESCRIPTION VARCHAR(200),
	START_DATE DATE,
	END_DATE DATE,
	ESTIMATED_COST DOUBLE,
	CURRENT_COST DOUBLE,
	SS_BUDGET DOUBLE,
	JS_BUDGET DOUBLE,
	DS_BUDGET DOUBLE,
	P1_BUDGET DOUBLE,
	P2_BUDGET DOUBLE,
	P3_BUDGET DOUBLE,
	P4_BUDGET DOUBLE,
	P5_BUDGET DOUBLE,
	P6_BUDGET DOUBLE,
	PARENT_PACKAGE_ID VARCHAR(50),
	LEAF BIT(1) NOT NULL DEFAULT b'0',
	PRIMARY KEY(PACKAGE_ID, PROJECT_ID)
);

-- CREATE TABLE STATUS_REPORT
CREATE TABLE COMP4911_DB.STATUS_REPORT
(
	ID integer not null auto_increment,
	PACKAGE_ID VARCHAR(20),
	PROJECT_ID VARCHAR(50),
	ENTRY_DATE DATE,
	SS_ESTIMATE DOUBLE,
	JS_ESTIMATE DOUBLE,
	DS_ESTIMATE DOUBLE,
	P1_ESTIMATE DOUBLE,
	P2_ESTIMATE DOUBLE,
	P3_ESTIMATE DOUBLE,
	P4_ESTIMATE DOUBLE,
	P5_ESTIMATE DOUBLE,
	P6_ESTIMATE DOUBLE,
	COMMENTS VARCHAR(200),
	PRIMARY KEY(ID)
);

-- CREATE TABLE EMPLOYEE_PROJECT_ASSIGNMENT
CREATE TABLE COMP4911_DB.EMPLOYEE_PROJECT_ASSIGNMENT
(
	PROJECT_ID VARCHAR(20),
	EMPLOYEE_USERNAME VARCHAR(50),
	PRIMARY KEY(PROJECT_ID, EMPLOYEE_USERNAME)
);

-- CREATE TABLE LABOUR_GRADE_CHARGES
CREATE TABLE COMP4911_DB.LABOUR_GRADE_CHARGE_RATE
(
	ID integer not null auto_increment,
	ENTRY_DATE DATE,
	SS_CHARGE_RATE DOUBLE,
	JS_CHARGE_RATE DOUBLE,
	DS_CHARGE_RATE DOUBLE,
	P1_CHARGE_RATE DOUBLE,
	P2_CHARGE_RATE DOUBLE,
	P3_CHARGE_RATE DOUBLE,
	P4_CHARGE_RATE DOUBLE,
	P5_CHARGE_RATE DOUBLE,
	P6_CHARGE_RATE DOUBLE,
	PRIMARY KEY(ID)
);



-- CREATE TABLE LABOUR_GRADES
CREATE TABLE COMP4911_DB.LABOUR_GRADES
(
	LABOUR_GRADE_ID VARCHAR(20),
	LABOUR_GRADE_TITLE VARCHAR(50),
	PRIMARY KEY(LABOUR_GRADE_ID)
);


create table COMP4911_DB.timesheet (
        id integer not null auto_increment,
        endWeek date,
        flextime decimal(5,1),
        overtime decimal(5,1),
        employee_ID varchar(255),
        needsReviewing bit(1) NOT NULL DEFAULT b'0',
        approved bit(1) NOT NULL DEFAULT b'0',
        denied bit(1) NOT NULL DEFAULT b'0',
        primary key (id)
    ) ;

    create table COMP4911_DB.timesheetRow (
        id integer not null auto_increment,
        workPackage varchar(255),
        projectID varchar(255),
        sat decimal(3,1) DEFAULT '0.0',
        sun decimal(3,1) DEFAULT '0.0',
        mon decimal(3,1) DEFAULT '0.0',
        tue decimal(3,1) DEFAULT '0.0',
        wed decimal(3,1) DEFAULT '0.0',
        thu decimal(3,1) DEFAULT '0.0',
        fri decimal(3,1) DEFAULT '0.0',
        notes varchar(255),
        timesheet_id integer,
        labourGradeID varchar(255),
        primary key (id)
    ) ;

-- CREATE VIEW TIMESHEET_LG_VIEW
CREATE VIEW COMP4911_DB.TIMESHEET_LG_VIEW AS ( 
	SELECT
	WORKPACKAGE AS WORK_PACKAGE, 
	PROJECTID AS PROJECT_ID, 
	LABOURGRADEID AS LABOUR_GRADE_ID, 
	SUM(MON) AS mon, 
    SUM(TUE) AS tue, 
	SUM(WED) AS wed, 
	SUM(THU) AS thu, 
	SUM(FRI) AS fri, 
	SUM(SAT) AS sat, 
	SUM(SUN) AS sun,
    SUM(MON + TUE + WED + THU + FRI + SAT + SUN) AS TOTAL_SUM
    FROM COMP4911_DB.TIMESHEETROW
    INNER JOIN COMP4911_DB.TIMESHEET
    ON COMP4911_DB.TIMESHEETROW.timesheet_id = COMP4911_DB.TIMESHEET.id
	WHERE PROJECTID  = 1 AND TIMESHEET.approved = 1
	GROUP BY WORK_PACKAGE, LABOUR_GRADE_ID, PROJECT_ID
);

    alter table COMP4911_DB.timesheet 
        add constraint FK_ijmohmv5u2yndgy2jjt82glie 
        foreign key (employee_ID) 
        references employee (employee_ID);

    alter table COMP4911_DB.timesheetRow 
        add constraint FK_7hl940963etg8tn4q1lpl26j4 
        foreign key (timesheet_id) 
        references timesheet (id);
	

-- Load data into EMPLOYEE table
INSERT INTO COMP4911_DB.EMPLOYEE VALUES('0', 'SS','Admin', '','admin','admin','admin','not available', 'not available', 'not available', 'not available', '111-111-1111', '111-111-1111', 'test@test.test',  0, 0);

-- Load data into CREDENTIALS table
INSERT INTO COMP4911_DB.CREDENTIALS VALUES('admin','123');

-- Load data into LABOUR_GRADES table
INSERT INTO COMP4911_DB.LABOUR_GRADES VALUES('SS','SS');
INSERT INTO COMP4911_DB.LABOUR_GRADES VALUES('JS','JS');
INSERT INTO COMP4911_DB.LABOUR_GRADES VALUES('DS','DS');
INSERT INTO COMP4911_DB.LABOUR_GRADES VALUES('P1','P1');
INSERT INTO COMP4911_DB.LABOUR_GRADES VALUES('P2','P2');
INSERT INTO COMP4911_DB.LABOUR_GRADES VALUES('P3','P3');
INSERT INTO COMP4911_DB.LABOUR_GRADES VALUES('P4','P4');
INSERT INTO COMP4911_DB.LABOUR_GRADES VALUES('P5','P5');
INSERT INTO COMP4911_DB.LABOUR_GRADES VALUES('P6','P6');

-- Load data into LABOUR_GRADE_CHARGE_RATE table
INSERT INTO COMP4911_DB.LABOUR_GRADE_CHARGE_RATE VALUES(1,'2014-03-30',9.0,8.0,7.0,6.0,5.0,4.0,3.0,2.0,1.0);
