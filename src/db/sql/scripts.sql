CREATE TABLE IF NOT EXISTS "user" (
	id SERIAL NOT NULL,
	active BOOLEAN NULL,
	email VARCHAR(255) NULL,
	name VARCHAR(255) NULL,
	password VARCHAR(255) NULL,
	surname VARCHAR(255) NULL,
	username VARCHAR(255) NULL,
	CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_login (
	id SERIAL NOT NULL,
	username VARCHAR(255) NOT NULL,
	session_id VARCHAR(255) NOT NULL,
	active BOOL NULL DEFAULT TRUE,
	CONSTRAINT user_login_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_activation_link (
	link_id UUID NOT NULL,
	username VARCHAR(255) NOT NULL,
	expired BOOL NOT NULL DEFAULT FALSE,
	CONSTRAINT user_activation_link_pkey PRIMARY KEY (link_id)
);

CREATE TABLE IF NOT EXISTS priority (
	id BIGSERIAL NOT NULL,
	value VARCHAR(255) NOT NULL,
	CONSTRAINT priorities_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS task (
	id BIGSERIAL NOT NULL,
	complete_date TIMESTAMP(6) NULL,
	completed BOOL NOT NULL DEFAULT FALSE,
	deadline TIMESTAMP(6) NULL,
	"name" VARCHAR(255) NULL,
	assignee_id int8 NULL,
	creator_id int8 NULL,
	priority_id int8 NULL,
--	category varchar(255) NULL,
--	CONSTRAINT task_category_check CHECK (((category)::text = ANY ((ARRAY['ENGINEERING'::character varying, 'SALES'::character varying, 'DOCUMENTATION'::character varying, 'WEB_DESIGN'::character varying, 'TESTING'::character varying])::text[]))),
	CONSTRAINT task_pkey PRIMARY KEY (id)
);
ALTER TABLE task DROP CONSTRAINT IF EXISTS fkekr1dgiqktpyoip3qmp6lxsit;
ALTER TABLE task DROP CONSTRAINT IF EXISTS fknq0d4mra8tpuwwak86ctvhfsb;
ALTER TABLE task DROP CONSTRAINT IF EXISTS fkt1ph5sat39g9lpa4g5kl46tbv;
ALTER TABLE task ADD CONSTRAINT fkekr1dgiqktpyoip3qmp6lxsit FOREIGN KEY (assignee_id) REFERENCES "user"(id);
ALTER TABLE task ADD CONSTRAINT fknq0d4mra8tpuwwak86ctvhfsb FOREIGN KEY (priority_id) REFERENCES priority(id);
ALTER TABLE task ADD CONSTRAINT fkt1ph5sat39g9lpa4g5kl46tbv FOREIGN KEY (creator_id) REFERENCES "user"(id);
