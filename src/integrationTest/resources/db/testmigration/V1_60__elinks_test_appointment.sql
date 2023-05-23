
UPDATE dbjudicialdata.judicial_office_authorisation SET appointment_id='179' where personal_code='27';


INSERT INTO dbjudicialdata.judicial_user_profile (personal_code,known_as,surname,full_name,post_nominals,ejudiciary_email,active_flag,created_date,last_loaded_date,object_id,sidam_id,initials)
VALUES ('31','Test2Kb','Test2Sb','Test2Fb','Mr','test531@test.net','TRUE','2021-07-14 12:25:28.763','2021-07-14 12:25:28.763','1.11112E+12','1.11112E+12','J.K');


INSERT INTO dbjudicialdata.judicial_office_appointment
(judicial_office_appointment_id, base_location_id, region_id, is_prinicple_appointment,
start_date, end_date, created_date, last_loaded_date, personal_code,
epimms_id, service_code, object_id, appointment, appointment_type,appointment_id)
VALUES(193, '1029', '1', true, '1995-03-27', '2023-03-27',
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '31', NULL, 'AAA5', '1111122223333', 'TestApp', 'TestApptype','193'),
(194, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '31', NULL, 'ABA3', '1111122223333', 'TestApp', 'TestApptype','194');

INSERT INTO dbjudicialdata.judicial_office_authorisation
(judicial_office_auth_id, jurisdiction, start_date, end_date, created_date,
last_updated, lower_level, personal_code, ticket_code, object_id,authorisation_id,jurisdiction_id,appointment_id)
VALUES(1021, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', NULL,
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '31', NULL, '1111122223333',7,1,'193'),
(1022, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', NULL,
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '31', '373', '1111122223333',8,2,'194');

