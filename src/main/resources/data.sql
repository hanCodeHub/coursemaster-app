--------------------------------------------------
-- Pre-populate BU MET Course/Instructor Data
--------------------------------------------------
-- BU MET course data (currently based on summer and fall 2020 semesters)
INSERT INTO course(College, Department, CourseNumber, Title, Credits, Type, Description, AverageRating)
SELECT College, Department, CourseNumber, Title, Credits, Type, Description, AverageRating
FROM  CSVREAD('classpath:course_MET.csv');

-- BU MET instructor data (currently based on fall  and summer semesters)
INSERT INTO instructor(Name)
SELECT Name
FROM CSVREAD('classpath:instructor_MET.csv');

-- BU MET course/instructor join data
INSERT INTO course_instructor (course_id, instructor_id)
SELECT C.ID AS Course_ID, I.ID AS Instructor_ID
FROM CSVREAD('classpath:course_instructor_MET.csv') AS CICSV
INNER JOIN course AS C ON CICSV.College = C.College
                      AND CICSV.Department = C.Department
                      AND CICSV.CourseNumber = C.CourseNumber
                      AND CICSV.Type = C.Type
INNER JOIN instructor AS I ON CICSV.Instructor = I.Name;


--------------------------------------------------
-- Add fake data for testing
--------------------------------------------------
-- Fake ROLE_STUDENTS data
insert into user (username, email, role, oauth2id) values ('HanYolo', 'han@ustream.tv', 'ROLE_STUDENT','jcpper0');
insert into user (username, email, role, oauth2id) values ('MarkShaulPetCat', 'mark@loc.gov', 'ROLE_STUDENT','briche');
insert into user (username, email, role, oauth2id) values ('RobTheRodge', 'rob@de.vu', 'ROLE_STUDENT','bann');
insert into user (username, email, role, oauth2id) values ('cshurlock3', 'khanratty3@go.com', 'ROLE_STUDENT','cshur');
insert into user (username, email, role, oauth2id) values ('fply4', 'cbeechcraft4@addtoany.com', 'ROLE_STUDENT','fply');
insert into user (username, email, role, oauth2id) values ('jhales5', 'dmanuaud5@census.gov', 'ROLE_STUDENT','jhale');
insert into user (username, email, role, oauth2id) values ('kknaggs6', 'kleward6@twitter.com', 'ROLE_STUDENT','kknage');
insert into user (username, email, role, oauth2id) values ('gminney7', 'kdawber7@ezinearticles.com', 'ROLE_STUDENT','gmine');
insert into user (username, email, role, oauth2id) values ('abevans8', 'dbeynke8@princeton.edu', 'ROLE_STUDENT','aben');
insert into user (username, email, role, oauth2id) values ('raikman9', 'bwhaley9@yellowbook.com', 'ROLE_STUDENT','raikm');

-- Fake REVIEW data
insert into review (instructor_id, user_id, course_id, courseDate, rating, comment)
        values (48, 1, 1, TO_DATE('14-07-2017', 'DD-MM-YYYY'), 3.8, 'Nullam accumsan enim quis tincidunt vestibulum. Nullam auctor tincidunt augue a mattis. Nam mollis lobortis metus eu ultricies.');
insert into review (instructor_id, user_id, course_id, courseDate, rating, comment)
        values (48, 2, 1, TO_DATE('26-12-2018', 'DD-MM-YYYY'), 4.3, 'Nam eu arcu nec urna semper egestas at quis ligula. Sed nec ligula cursus, tempor justo vitae, pretium est. Sed vel mi ut lorem ornare feugiat.');
insert into review (instructor_id, user_id, course_id, courseDate, rating, comment)
        values (48, 3, 1, TO_DATE('23-11-2019', 'DD-MM-YYYY'), 4.3, 'Nam eu arcu nec urna semper egestas at quis ligula. Sed nec ligula cursus, tempor justo vitae, pretium est. Sed vel mi ut lorem ornare feugiat.');
