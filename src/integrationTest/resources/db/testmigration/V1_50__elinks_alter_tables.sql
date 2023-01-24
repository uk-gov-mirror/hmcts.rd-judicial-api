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