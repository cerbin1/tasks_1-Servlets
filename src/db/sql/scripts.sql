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
