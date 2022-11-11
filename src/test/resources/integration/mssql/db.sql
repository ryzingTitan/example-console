USE [master];
CREATE DATABASE test;
USE [test];

CREATE SCHEMA [test_schema];

CREATE TABLE [test_schema].[user](
    [id]            [int] IDENTITY(1,1) NOT NULL,
    [username]      [varchar](30)       NOT NULL,
    [first_name]    [varchar](30)       NOT NULL,
    [last_name]     [varchar](30)       NOT NULL,
);

INSERT INTO [test_schema].[user](username, first_name, last_name) VALUES ('testUser4', 'distracted', 'kalam');
INSERT INTO [test_schema].[user](username, first_name, last_name) VALUES ('testUser5', 'focused', 'swartz');
INSERT INTO [test_schema].[user](username, first_name, last_name) VALUES ('testUser6', 'focused', 'kilby');

CREATE LOGIN test WITH PASSWORD = 'SecurePassw0rd';
CREATE USER test FOR LOGIN test;

CREATE ROLE SuperTest;
GRANT SELECT TO SuperTest;

EXEC sp_addrolemember 'SuperTest', test;