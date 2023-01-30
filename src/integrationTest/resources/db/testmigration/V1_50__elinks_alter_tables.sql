--Create sequence for dbjudicialdata.judicial_office_authorisation
CREATE SEQUENCE dbjudicialdata.judicial_office_auth_id_sequence AS integer START 1 OWNED
BY dbjudicialdata.judicial_office_authorisation.judicial_office_auth_id;

ALTER TABLE dbjudicialdata.judicial_office_authorisation
ALTER COLUMN judicial_office_auth_id SET DEFAULT nextval('dbjudicialdata.judicial_office_auth_id_sequence');

--Create sequence for dbjudicialdata.judicial_office_appointment
CREATE SEQUENCE dbjudicialdata.judicial_office_appointment_id_sequence AS integer START 1 OWNED
BY dbjudicialdata.judicial_office_appointment.judicial_office_appointment_id;


ALTER TABLE dbjudicialdata.judicial_office_appointment
ALTER COLUMN judicial_office_appointment_id SET DEFAULT nextval('dbjudicialdata.judicial_office_appointment_id_sequence');

insert into dbjudicialdata.base_location_type(base_location_id,court_name,court_type,circuit,area_of_expertise)
values ('0', 'default', 'default','default', 'default');

insert into dbjudicialdata.region_type(region_id,region_desc_en,region_desc_cy)
values ('0', 'default', 'default');


CREATE TABLE dbjudicialdata.judicial_location_mapping (
	epimms_id varchar(16),
	judicial_base_location_id varchar(64),
	building_location_name varchar(256),
	base_location_name varchar(128),
	service_code varchar(16)
);


CREATE SEQUENCE dbjudicialdata.elink_audit_scheduler_id_sequence AS integer START 1 OWNED
BY dbjudicialdata.dataload_schedular_audit.id;


ALTER TABLE dbjudicialdata.dataload_schedular_audit
ALTER COLUMN id SET DEFAULT nextval('dbjudicialdata.elink_audit_scheduler_id_sequence');

--insert test data

insert into dbjudicialdata.judicial_user_profile (personal_code, known_as, surname, full_name, post_nominals, ejudiciary_email, last_working_date, active_flag, created_date, last_loaded_date, object_id, sidam_id, initials) values ('Emp', 'TestEmp', 'Test', 'Test1','Test Test1', 'abc@gmail.com', current_date, 'true' , current_timestamp, current_timestamp, '87463688-4b00-e2e7-4ff8-281b87f16bf9', '44862987-4b00-e2e7-4ff8-281b87f16bf9',  'TT1');
insert into dbjudicialdata.judicial_user_profile (personal_code, known_as, surname, full_name, post_nominals, ejudiciary_email, last_working_date, active_flag, created_date, last_loaded_date, object_id, sidam_id, initials) values ('Emp2', 'TestEmp', 'Test', 'Test2','Test Test2', 'abc2@gmail.com', current_date, 'true', current_timestamp, current_timestamp, '4c0ff6a3-8fd6-803b-301a-281b87f16bf8', '4c0ff6a3-8fd6-803b-301a-29d9dacccca8',  'TT2');


INSERT INTO dbjudicialdata.base_location_type
(base_location_id, court_name, court_type, circuit, area_of_expertise)
VALUES('1029', 'Aberconwy', 'Old Gwynedd', 'National', 'LJA');

INSERT INTO dbjudicialdata.region_type
(region_id, region_desc_en, region_desc_cy)
VALUES('1', 'National', NULL);

INSERT INTO dbjudicialdata.judicial_user_profile
(personal_code, known_as, surname, full_name, post_nominals, ejudiciary_email,
last_working_date, active_flag, created_date,
last_loaded_date, object_id, sidam_id, initials)
VALUES('A123', 'Sample_KA_1', 'Sample_SN_1', 'Sample_FN_1', 'Ms', 'test900@test.net', NULL, true,
'2022-03-01 12:25:28.763', '2022-03-01 09:10:44.682', '900', '900', 'AA'),
('A124', 'Am''ar', 'Pamet', 'Am''ar Pamet', 'Ms', 'test802@test.net', NULL, true,
'2022-03-01 09:10:44.682', '2022-03-01 09:10:44.682', '802', '802', 'AB'),
('A125', 'O''jas', 'Baet', 'O''jas Baet', 'Ms', 'test803@test.net', NULL, true,
'2022-03-01 09:10:44.682', '2022-03-01 09:10:44.682', '803', '803', 'CC'),
('A126', 'Li-am', 'Kate', 'Li-am Kate', 'Ms', 'test804@test.net', NULL, true,
'2022-03-01 09:10:44.682', '2022-03-01 09:10:44.682', '804', '804', 'CD'),
('A127', 'V-ed', 'Prakasscs', 'V-ed Prakasscs', 'Ms', 'test805@test.net', NULL, true,
'2022-03-01 09:10:44.682', '2022-03-01 09:10:44.682', '805', '805', 'DE'),
('A128', 'J Rock', 'Brian', 'J Rock Brian', 'Ms', 'test806@test.net', NULL, true,
'2022-03-01 09:10:44.682', '2022-03-01 09:10:44.682', '806', '806', 'EE'),
('A129', 'To Nick', 'Cruz', 'To Nick Cruz', 'Ms', 'test807@test.net', NULL, true,
'2022-03-01 09:10:44.682', '2022-03-01 09:10:44.682', '807', '807', 'EF');

INSERT INTO dbjudicialdata.judicial_office_appointment
(judicial_office_appointment_id, base_location_id, region_id, is_prinicple_appointment,
start_date, end_date, created_date, last_loaded_date, personal_code,
epimms_id, service_code, object_id, appointment, appointment_type)
VALUES
(9001, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', 'A123', '20014', 'BBA4', '1111', 'refreshTest', 'TestApptype');

INSERT INTO dbjudicialdata.judicial_user_profile
(personal_code, known_as, surname, full_name, post_nominals, ejudiciary_email,
last_working_date, active_flag, created_date,
last_loaded_date, object_id, sidam_id, initials)
VALUES
('29', 'Refresh1KA', 'Refresh1SN', 'Refresh1FN', 'Ms', 'test1001@test.net',
NULL, true, '2021-08-11 09:10:44.682', '2021-08-11 09:10:44.682',
'1111', '1111', 'RR'),
('4923413', 'Joe', 'Bloggs', 'Joe Bloggs', 'Mr', 'EMP40399@ejudiciary.net',
NULL, true, '2021-08-11 09:10:44.682', '2021-08-11 09:10:44.682',
'd4774030-32cc-4b64-894f-d475b0b1129c', '', 'JB'),
('4923818', 'Joe', 'Bloggs', 'Joe Bloggs', 'Mr', 'EMP40704@ejudiciary.net',
NULL, true, '2021-08-11 09:10:44.682', '2021-08-11 09:10:44.682',
'fcb4f03c-4b3f-4c3c-bf3a-662b4557b470', '', 'JBJ');

INSERT INTO dbjudicialdata.judicial_office_appointment
(judicial_office_appointment_id, base_location_id, region_id, is_prinicple_appointment,
start_date, end_date, created_date, last_loaded_date, personal_code,
epimms_id, service_code, object_id, appointment, appointment_type, work_pattern)
VALUES
(1001, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '29', '20014', 'BBA4', '1111', 'refreshTest', 'TestApptype', 'No Work Pattern (M to F)'),
(1002, '1029', '1', true, '2006-12-11', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '4923413', '20014', 'BBA4', 'd4774030-32cc-4b64-894f-d475b0b1129c', 'Tribunal Judge', 'Fee Paid', 'Fee Paid Judiciary 5 Days Mon - Fri'),
(1003, '1029', '1', true, '2008-03-11', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '4923818', '20014', 'BBA4', 'fcb4f03c-4b3f-4c3c-bf3a-662b4557b470', 'Tribunal Judge', 'Fee Paid', 'Fee Paid Judiciary 5 Days Mon - Fri');

INSERT INTO dbjudicialdata.judicial_office_authorisation
(judicial_office_auth_id, jurisdiction, start_date, end_date, created_date,
last_updated, lower_level, personal_code, ticket_code, object_id)
VALUES
(1001, 'Authorisation Magistrate', '2002-09-09', NULL,
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '29', '364', '1111'),
(1002, 'Authorisation Tribunals', '2006-12-11 00:00:00.000', NULL,
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'First Tier - Immigration and Asylum', '4923413', '364', 'd4774030-32cc-4b64-894f-d475b0b1129c'),
(1003, 'Authorisation Tribunals', '2018-09-10 00:00:00.000', '2020-02-14 00:00:00.000',
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Criminal Injuries Compensations', '4923818', '364', 'fcb4f03c-4b3f-4c3c-bf3a-662b4557b470'),
(1004, 'Authorisation Tribunals', null, '2020-02-14 00:00:00.000',
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'First Tier - Tax', '4923818', '364', 'fcb4f03c-4b3f-4c3c-bf3a-662b4557b470'),
(1005, 'Authorisation Tribunals', '2018-09-10 00:00:00.000', '2020-02-14 00:00:00.000',
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'First Tier - Immigration and Asylum', '4923818', '364', 'fcb4f03c-4b3f-4c3c-bf3a-662b4557b470'),
(1006, 'Authorisation Tribunals', '1901-01-01 00:00:00.000', '2020-02-14 00:00:00.000',
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', '04 - Incapacity Benefit Employment Support', '4923818', '364', 'fcb4f03c-4b3f-4c3c-bf3a-662b4557b470'),
(1007, 'Authorisation Tribunals', '1901-01-01 00:00:00.000', '2020-02-14 00:00:00.000',
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', '01 - Social Security', '4923818', '364', 'fcb4f03c-4b3f-4c3c-bf3a-662b4557b470'),
(1008, 'Authorisation Tribunals', '1901-01-01 00:00:00.000', '2020-02-14 00:00:00.000',
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', '03 - Disability Living Allowance', '4923818', '364', 'fcb4f03c-4b3f-4c3c-bf3a-662b4557b470'),
(1009, 'Authorisation Tribunals', null, '2020-02-14 00:00:00.000',
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'First Tier - Social Entitlement', '4923818', '364', 'fcb4f03c-4b3f-4c3c-bf3a-662b4557b470'),
(1010, 'Authorisation Tribunals', null, '2019-03-31 00:00:00.000',
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Asylum Support', '4923818', '364', 'fcb4f03c-4b3f-4c3c-bf3a-662b4557b470');

INSERT INTO dbjudicialdata.judicial_role_type
(role_id, personal_code, title, start_date, end_date)
values
(1, '29', 'testTitle', '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054'),
(2, '29', 'testTitle1', '2021-08-11 09:14:30.054', current_timestamp + interval '30' day),
(3, '29', 'testTitle2', '2021-08-11 09:14:30.054', null);

INSERT INTO dbjudicialdata.judicial_user_profile
(personal_code, known_as, surname, full_name, post_nominals,
ejudiciary_email, last_working_date, active_flag, created_date,
last_loaded_date, object_id, sidam_id)
VALUES('277', 'Test2KA', 'Test2SN', 'Test2FN', 'Ms', 'test528@test.net', NULL, true,
'2021-08-11 09:10:44.682', '2021-08-11 09:10:44.682',
'1111122223333', '1111122223333'),
('288', 'TestKA', 'TestSN', 'TestFN', 'Ms', 'test529@test.net', NULL, true,
'2021-08-11 09:10:44.682', '2021-08-11 09:10:44.682',
'1111122223333', '1111122223334'),
('299', 'Test1KA', 'Test1SN', 'Test1FN', 'Ms', 'test530@test.net',NULL, true,
'2021-08-11 09:10:44.682', '2021-08-11 09:10:44.682',
'1111122223333', '1111122223334');

INSERT INTO dbjudicialdata.judicial_office_appointment
(judicial_office_appointment_id, base_location_id, region_id, is_prinicple_appointment,
start_date, end_date, created_date, last_loaded_date, personal_code,
epimms_id, service_code, object_id, appointment, appointment_type, work_pattern)
VALUES(179, '1029', '1', true, '1995-03-27', NULL, '2021-07-14 12:25:26.330',
'2021-08-11 09:12:40.134', '277', NULL, 'BFA1', '1111122223333', 'TestApp', 'TestApptype', 'No Work Pattern (M to F)'),
(180, '1029', '1', true, '1995-03-27', NULL, '2021-07-14 12:25:26.330',
'2021-08-11 09:12:40.134', '277', NULL, 'BBA3', '1111122223333', 'TestApp', 'TestApptype', 'No Work Pattern (M to F)'),
(181, '1029', '1', true, '1995-03-27', NULL, '2021-07-14 12:25:26.330',
'2021-08-11 09:12:40.134', '288', NULL, NULL, '1111122223333', 'TestApp', 'TestApptype', 'Fee Paid Judiciary 5 Days Mon - Fri'),
(182, '1029', '1', true, '1995-03-27', NULL, '2021-07-14 12:25:26.330',
'2021-08-11 09:12:40.134', '299', '20012', 'BFA1', '1111122223333', 'TestApp', 'TestApptype', 'Fee Paid Judiciary 5 Days Mon - Fri'),
(183, '1029', '1', true, '1995-03-27', NULL, '2021-07-14 12:25:26.330',
'2021-08-11 09:12:40.134', '277', '20013', 'BBA3', '1111122223333', 'TestApp', 'TestApptype', 'No Work Pattern (M to F)'),
(184, '1029', '1', true, '1995-03-27', NULL, '2021-07-14 12:25:26.330',
'2021-08-11 09:12:40.134', '277', '20014', 'AAA6', '1111122223333', 'TestApp', 'TestApptype', 'No Work Pattern (M to F)'),
(185, '1029', '1', true, '1995-03-27', NULL, '2021-07-14 12:25:26.330',
'2021-08-11 09:12:40.134', '277', '20014', 'AAA7', '1111122223333', 'TestApp', 'TestApptype', 'No Work Pattern (M to F)'),
(186, '1029', '1', true, '1995-03-27', NULL, '2021-07-14 12:25:26.330',
'2021-08-11 09:12:40.134', '277', '20014', 'ABA3', '1111122223333', 'TestApp', 'TestApptype', 'No Work Pattern (M to F)'),
(187, '1029', '1', true, '1995-03-27', NULL, '2021-07-14 12:25:26.330',
'2021-08-11 09:12:40.134', '277', '20014', 'ABA5', '1111122223333', 'TestApp', 'TestApptype', 'No Work Pattern (M to F)');

INSERT INTO dbjudicialdata.judicial_office_authorisation
(judicial_office_auth_id, jurisdiction, start_date, end_date, created_date,
last_updated, lower_level, personal_code, ticket_code, object_id)
VALUES(7, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', NULL,
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '277', NULL, '1111122223333'),
(8, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', NULL,
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '277', '373', '1111122223333'),
(9, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', NULL,
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '288', '364', '1111122223333');

INSERT INTO dbjudicialdata.judicial_user_profile (personal_code,known_as,surname,full_name,post_nominals,ejudiciary_email,last_working_date,active_flag,created_date,last_loaded_date,object_id,sidam_id) VALUES
('A345','Joe','Bloggs','Joe Bloggs','Mrs','employee@ejudiciary.net',NULL,true,'2022-05-09 13:13:49.225242','2022-05-09 13:13:49.225242','74ac97ad',NULL);

INSERT INTO dbjudicialdata.judicial_office_authorisation (judicial_office_auth_id,jurisdiction,start_date,end_date,created_date,last_updated,lower_level,personal_code,object_id,ticket_code) VALUES
(1011,'Authorisation Tribunals','2018-08-22 00:00:00',NULL,'2022-05-09 13:20:01.997931','2022-05-09 13:20:01.997931','First Tier - Social Entitlement','A345','74ac97ad',NULL),
(1012,'Authorisation Tribunals','2018-08-22 00:00:00',NULL,'2022-05-09 13:20:01.997931','2022-05-09 13:20:01.997931','Social Security and Child Support','A345','74ac97ad','357'),
(1013,'Authorisation Tribunals','2018-08-22 00:00:00','2060-11-21 00:00:00','2022-05-09 13:20:01.997931','2022-05-09 13:20:01.997931','Child Support 02','A345','74ac97ad',NULL);

INSERT INTO dbjudicialdata.judicial_office_appointment (judicial_office_appointment_id,base_location_id,region_id,is_prinicple_appointment,start_date,end_date,created_date,last_loaded_date,personal_code,epimms_id,service_code,object_id,appointment,appointment_type) VALUES
(100,'1029','1',true,'2018-08-22',NULL,'2022-05-09 13:17:32.506915','2022-05-09 13:17:32.506915','A345','','BBA3','74ac97ad','Tribunal Member Disability','Fee Paid');