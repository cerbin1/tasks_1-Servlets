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

CREATE TABLE IF NOT EXISTS notification (
	id bigserial NOT NULL,
	create_date timestamp(6) NOT NULL,
	"name" varchar(255) NOT NULL,
	"read" bool NOT NULL DEFAULT false,
	read_date timestamp(6) NULL,
	task_id int8 NOT NULL,
	user_id int8 NOT NULL,
	CONSTRAINT notification_pkey PRIMARY KEY (id)
);
ALTER TABLE notification DROP CONSTRAINT IF EXISTS fk2ktjq1slw0ldkuy5rx8fbte2p;
ALTER TABLE notification DROP CONSTRAINT IF EXISTS fk9y21adhxn0ayjhfocscqox7bh;
ALTER TABLE notification ADD CONSTRAINT fk2ktjq1slw0ldkuy5rx8fbte2p FOREIGN KEY (task_id) REFERENCES task(id);
ALTER TABLE notification ADD CONSTRAINT fk9y21adhxn0ayjhfocscqox7bh FOREIGN KEY (user_id) REFERENCES "user"(id);

CREATE TABLE IF NOT EXISTS task_reminder (
	id bigserial NOT NULL,
	sent bool NOT NULL DEFAULT false,
	task_id int8 NOT NULL,
	planned_send_date timestamp(6) NOT NULL,
	sent_date timestamp(6) NULL,
	CONSTRAINT task_reminder_pkey PRIMARY KEY (id),
	CONSTRAINT uk_r27tf7809aiy305ogagleyl8x UNIQUE (task_id)
);
ALTER TABLE task_reminder DROP CONSTRAINT IF EXISTS fkd028lj86vw4icrkf6ss76njp3;
ALTER TABLE task_reminder ADD CONSTRAINT fkd028lj86vw4icrkf6ss76njp3 FOREIGN KEY (task_id) REFERENCES task(id);


CREATE TABLE IF NOT EXISTS chat_message (
	id bigserial NOT NULL,
	"content" varchar(255) NOT NULL,
	sent_at timestamp(6) NOT NULL,
	"sequence" int4 NOT NULL,
	sender_id int8 NOT NULL,
	task_id int8 NOT NULL,
	CONSTRAINT chat_message_pkey PRIMARY KEY (id)
);
ALTER TABLE chat_message DROP CONSTRAINT IF EXISTS fke3tn55xm4h4uog1wgawrx873y;
ALTER TABLE chat_message DROP CONSTRAINT IF EXISTS fkgiqeap8ays4lf684x7m0r2729;
ALTER TABLE chat_message ADD CONSTRAINT fke3tn55xm4h4uog1wgawrx873y FOREIGN KEY (task_id) REFERENCES task(id);
ALTER TABLE chat_message ADD CONSTRAINT fkgiqeap8ays4lf684x7m0r2729 FOREIGN KEY (sender_id) REFERENCES "user"(id);

CREATE TABLE IF NOT EXISTS subtask (
	id bigserial NOT NULL,
	"name" varchar(255) NOT NULL,
	"sequence" int8 NOT NULL,
	task_id int8 NULL,
	CONSTRAINT subtask_pkey PRIMARY KEY (id)
);
ALTER TABLE subtask DROP CONSTRAINT IF EXISTS fksvs126nsj9ohhvwjog5ddp76x;
ALTER TABLE subtask ADD CONSTRAINT fksvs126nsj9ohhvwjog5ddp76x FOREIGN KEY (task_id) REFERENCES task(id);

CREATE TABLE task_file (
	"name" varchar(255) NOT NULL,
	"type" varchar(255) NOT NULL,
	task_id int8 NOT NULL,
	CONSTRAINT task_file_pkey PRIMARY KEY ("name")
);
ALTER TABLE task_file DROP CONSTRAINT IF EXISTS fkgr48xj5f1r0t32jwf648vuoay;
ALTER TABLE task_file ADD CONSTRAINT fkgr48xj5f1r0t32jwf648vuoay FOREIGN KEY (task_id) REFERENCES task(id);
