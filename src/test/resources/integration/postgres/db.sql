CREATE SCHEMA test_schema;

ALTER SCHEMA test_schema OWNER TO test;

CREATE TABLE test_schema.user
(
    id          integer                 NOT NULL,
    username    character varying(30)   NOT NULL,
    first_name  character varying(30)   NOT NULL,
    last_name   character varying(30)   NOT NULL
);

ALTER TABLE test_schema.user OWNER TO test;

CREATE SEQUENCE test_schema.user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test_schema.user_id_seq OWNED BY test_schema.user.id;

ALTER TABLE ONLY test_schema.user ALTER COLUMN id SET DEFAULT nextval('test_schema.user_id_seq'::regclass);

SELECT pg_catalog.setval('test_schema.user_id_seq', 1, false);

INSERT INTO test_schema.user(username, first_name, last_name) VALUES ('testUser1', 'distracted', 'kalam');
INSERT INTO test_schema.user(username, first_name, last_name) VALUES ('testUser2', 'focused', 'swartz');
INSERT INTO test_schema.user(username, first_name, last_name) VALUES ('testUser3', 'focused', 'kilby');