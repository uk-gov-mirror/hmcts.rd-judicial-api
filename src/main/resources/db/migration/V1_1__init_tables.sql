-- DROP SCHEMA dbjudicialdata;

-- NB Flyway requires lowercase for table names
create schema if not exists dbjudicialdata;

-- DROP SEQUENCE dbjudicialdata.dataload_exception_records_id_seq;

CREATE SEQUENCE dbjudicialdata.dataload_exception_records_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.dataload_schedular_audit_id_seq;

CREATE SEQUENCE dbjudicialdata.dataload_schedular_audit_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.dataload_schedular_job_id_seq;

CREATE SEQUENCE dbjudicialdata.dataload_schedular_job_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.elink_audit_scheduler_id_sequence;

CREATE SEQUENCE dbjudicialdata.elink_audit_scheduler_id_sequence
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.elink_dataload_schedular_job_id_sequence;

CREATE SEQUENCE dbjudicialdata.elink_dataload_schedular_job_id_sequence
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.elink_exception_records_id_sequence;

CREATE SEQUENCE dbjudicialdata.elink_exception_records_id_sequence
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.elinks_responses_id_seq;

CREATE SEQUENCE dbjudicialdata.elinks_responses_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.elinks_responses_sequence;

CREATE SEQUENCE dbjudicialdata.elinks_responses_sequence
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.judicial_office_appointment_id_sequence;

CREATE SEQUENCE dbjudicialdata.judicial_office_appointment_id_sequence
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.judicial_office_auth_id_sequence;

CREATE SEQUENCE dbjudicialdata.judicial_office_auth_id_sequence
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.judicial_role_type_id_sequence;

CREATE SEQUENCE dbjudicialdata.judicial_role_type_id_sequence
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;
-- DROP SEQUENCE dbjudicialdata.judicial_role_type_role_id_seq;

CREATE SEQUENCE dbjudicialdata.judicial_role_type_role_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 2147483647
START 1
CACHE 1
NO CYCLE;-- dbjudicialdata.dataload_exception_records definition

-- Drop table

-- DROP TABLE dbjudicialdata.dataload_exception_records;

CREATE TABLE dbjudicialdata.dataload_exception_records (
	id int4 DEFAULT nextval('dbjudicialdata.elink_exception_records_id_sequence'::regclass) NOT NULL,
table_name varchar(64) NULL,
scheduler_start_time timestamp NOT NULL,
scheduler_name varchar(64) NOT NULL,
"key" varchar(256) NULL,
field_in_error varchar(256) NULL,
error_description varchar(512) NULL,
updated_timestamp timestamp NOT NULL,
row_id varchar(256) NULL,
page_id int8 NULL,
CONSTRAINT dataload_exception_records_pk PRIMARY KEY (id)
);


-- dbjudicialdata.dataload_schedular_audit definition

-- Drop table

-- DROP TABLE dbjudicialdata.dataload_schedular_audit;

CREATE TABLE dbjudicialdata.dataload_schedular_audit (
	id int4 DEFAULT nextval('dbjudicialdata.elink_audit_scheduler_id_sequence'::regclass) NOT NULL,
scheduler_name varchar(64) NOT NULL,
scheduler_start_time timestamp NOT NULL,
scheduler_end_time timestamp NULL,
status varchar(32) NULL,
api_name varchar(128) NULL,
CONSTRAINT dataload_schedular_audit_pk PRIMARY KEY (id)
);


-- dbjudicialdata.dataload_schedular_job definition

-- Drop table

-- DROP TABLE dbjudicialdata.dataload_schedular_job;

CREATE TABLE dbjudicialdata.dataload_schedular_job (
	id serial4 NOT NULL,
	publishing_status varchar(16) NULL,
	job_start_time timestamp NOT NULL,
	job_end_time timestamp NULL,
CONSTRAINT dataload_schedular_job_pk PRIMARY KEY (id)
);


-- dbjudicialdata.elinks_responses definition

-- Drop table

-- DROP TABLE dbjudicialdata.elinks_responses;

CREATE TABLE dbjudicialdata.elinks_responses (
	id int4 DEFAULT nextval('dbjudicialdata.elinks_responses_sequence'::regclass) NOT NULL,
api_name varchar(64) NOT NULL,
elinks_data jsonb NOT NULL,
created_date timestamp NOT NULL,
CONSTRAINT elinks_responses_pk PRIMARY KEY (id)
);


-- dbjudicialdata.hmcts_region_type definition

-- Drop table

-- DROP TABLE dbjudicialdata.hmcts_region_type;

CREATE TABLE dbjudicialdata.hmcts_region_type (
	hmcts_region_id varchar(64) NOT NULL,
	hmcts_region_desc_en varchar(256) NOT NULL,
	hmcts_region_desc_cy varchar(256) NULL,
CONSTRAINT hmcts_region_id PRIMARY KEY (hmcts_region_id)
);


-- dbjudicialdata.jrd_lrd_region_mapping definition

-- Drop table

-- DROP TABLE dbjudicialdata.jrd_lrd_region_mapping;

CREATE TABLE dbjudicialdata.jrd_lrd_region_mapping (
	jrd_region_id varchar(16) NOT NULL,
	jrd_region varchar(256) NOT NULL,
	region_id varchar(16) NOT NULL,
	region varchar(256) NOT NULL
);


-- dbjudicialdata.judicial_location_mapping definition

-- Drop table

-- DROP TABLE dbjudicialdata.judicial_location_mapping;

CREATE TABLE dbjudicialdata.judicial_location_mapping (
	epimms_id varchar(16) NULL,
	judicial_base_location_id varchar(64) NULL,
	service_code varchar(16) NULL
);


-- dbjudicialdata.judicial_service_code_mapping definition

-- Drop table

-- DROP TABLE dbjudicialdata.judicial_service_code_mapping;

CREATE TABLE dbjudicialdata.judicial_service_code_mapping (
	service_id int8 NOT NULL,
	ticket_code varchar(16) NOT NULL,
	service_code varchar(64) NULL,
	service_description varchar(512) NULL,
CONSTRAINT service_id PRIMARY KEY (service_id)
);


-- dbjudicialdata.judicial_ticket_code_type definition

-- Drop table

-- DROP TABLE dbjudicialdata.judicial_ticket_code_type;

CREATE TABLE dbjudicialdata.judicial_ticket_code_type (
	ticket_code varchar(16) NOT NULL,
	ticket_category_id varchar(16) NOT NULL,
	lower_level varchar(256) NOT NULL
);


-- dbjudicialdata.judicial_user_profile definition

-- Drop table

-- DROP TABLE dbjudicialdata.judicial_user_profile;

CREATE TABLE dbjudicialdata.judicial_user_profile (
	personal_code varchar(32) NOT NULL,
	known_as varchar(256) NOT NULL,
	surname varchar(256) NOT NULL,
	full_name varchar(256) NOT NULL,
	post_nominals varchar(256) NULL,
	ejudiciary_email varchar(256) NULL,
	last_working_date date NULL,
	active_flag bool NULL,
	created_date timestamp NULL,
	last_loaded_date timestamp NULL,
	object_id varchar(64) NULL,
	sidam_id varchar(64) NULL,
	initials varchar(256) NULL,
	title varchar(256) NULL,
	retirement_date date NULL,
	deleted_flag bool NULL,
	date_of_deletion timestamp NULL,
CONSTRAINT judicial_user_profile_pkey PRIMARY KEY (personal_code)
);


-- dbjudicialdata.location_type definition

-- Drop table

-- DROP TABLE dbjudicialdata.location_type;

CREATE TABLE dbjudicialdata.location_type (
	base_location_id varchar(64) NOT NULL,
"name" varchar(256) NULL,
type_id varchar(64) NULL,
parent_id varchar(64) NULL,
jurisdiction_id varchar(64) NULL,
start_date timestamp NULL,
end_date timestamp NULL,
created_at timestamp NULL,
updated_at timestamp NULL,
CONSTRAINT base_location_id PRIMARY KEY (base_location_id)
);


-- dbjudicialdata.lock_details_provider definition

-- Drop table

-- DROP TABLE dbjudicialdata.lock_details_provider;

CREATE TABLE dbjudicialdata.lock_details_provider (
	"name" varchar(64) NOT NULL,
	lock_until timestamp NOT NULL,
	locked_at timestamp NOT NULL,
	locked_by varchar(255) NOT NULL,
CONSTRAINT lock_details_provider_pkey PRIMARY KEY (name)
);


-- dbjudicialdata.judicial_additional_roles definition

-- Drop table

-- DROP TABLE dbjudicialdata.judicial_additional_roles;

CREATE TABLE dbjudicialdata.judicial_additional_roles (
	role_id int4 DEFAULT nextval('dbjudicialdata.judicial_role_type_id_sequence'::regclass) NOT NULL,
personal_code varchar(32) NOT NULL,
title varchar(256) NOT NULL,
start_date timestamp NULL,
end_date timestamp NULL,
jurisdiction_role_id varchar(64) NOT NULL,
jurisdiction_role_name_id varchar(64) NOT NULL,
CONSTRAINT role_id PRIMARY KEY (role_id),
CONSTRAINT personal_code_fk FOREIGN KEY (personal_code) REFERENCES dbjudicialdata.judicial_user_profile(personal_code)
);


-- dbjudicialdata.judicial_office_appointment definition

-- Drop table

-- DROP TABLE dbjudicialdata.judicial_office_appointment;

CREATE TABLE dbjudicialdata.judicial_office_appointment (
	judicial_office_appointment_id int8 DEFAULT nextval('dbjudicialdata.judicial_office_appointment_id_sequence'::regclass) NOT NULL,
personal_code varchar(32) NOT NULL,
base_location_id varchar(64) NOT NULL,
hmcts_region_id varchar(64) NULL,
is_prinicple_appointment bool NULL,
start_date date NULL,
end_date date NULL,
created_date timestamp NULL,
last_loaded_date timestamp NULL,
epimms_id varchar(16) NULL,
appointment varchar(64) NOT NULL,
appointment_type varchar(32) NULL,
appointment_id varchar(256) NOT NULL,
role_name_id varchar(256) NULL,
"type" varchar(64) NULL,
contract_type_id varchar(64) NOT NULL,
"location" varchar(64) NULL,
jo_base_location_id varchar(64) NOT NULL,
CONSTRAINT judicial_office_appointment_appointment_id_key UNIQUE (appointment_id),
CONSTRAINT judicial_office_appointment_id PRIMARY KEY (judicial_office_appointment_id),
CONSTRAINT base_location_id_fk1 FOREIGN KEY (base_location_id) REFERENCES dbjudicialdata.location_type(base_location_id),
CONSTRAINT hmcts_region_id_fk1 FOREIGN KEY (hmcts_region_id) REFERENCES dbjudicialdata.hmcts_region_type(hmcts_region_id),
CONSTRAINT jo_base_location_id_fk1 FOREIGN KEY (jo_base_location_id) REFERENCES dbjudicialdata.location_type(base_location_id),
CONSTRAINT personal_code_fk FOREIGN KEY (personal_code) REFERENCES dbjudicialdata.judicial_user_profile(personal_code)
);


-- dbjudicialdata.judicial_office_authorisation definition

-- Drop table

-- DROP TABLE dbjudicialdata.judicial_office_authorisation;

CREATE TABLE dbjudicialdata.judicial_office_authorisation (
	judicial_office_auth_id int8 DEFAULT nextval('dbjudicialdata.judicial_office_auth_id_sequence'::regclass) NOT NULL,
personal_code varchar(32) NOT NULL,
jurisdiction varchar(256) NOT NULL,
start_date date NULL,
end_date date NULL,
created_date timestamp NULL,
last_updated timestamp NULL,
lower_level varchar(256) NOT NULL,
ticket_code varchar(16) NOT NULL,
appointment_id varchar(256) NOT NULL,
authorisation_id varchar(256) NOT NULL,
jurisdiction_id varchar(64) NOT NULL,
CONSTRAINT jud_auth_pk PRIMARY KEY (judicial_office_auth_id),
CONSTRAINT appointment_id_fk2 FOREIGN KEY (appointment_id) REFERENCES dbjudicialdata.judicial_office_appointment(appointment_id),
CONSTRAINT personal_code_fk FOREIGN KEY (personal_code) REFERENCES dbjudicialdata.judicial_user_profile(personal_code)
);