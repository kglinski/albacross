CREATE TABLE ip_scope
(
    id       BIGINT PRIMARY KEY NOT NULL,
    start_ip VARCHAR,
    end_ip   VARCHAR
);

CREATE SEQUENCE ip_scope_id_seq
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1;


CREATE FUNCTION generate_id() RETURNS trigger AS $set_id$
BEGIN
    IF NEW.id IS NULL THEN
        NEW.id = nextval('ip_scope_id_seq');
    END IF;
    RETURN NEW;
END;
$set_id$ LANGUAGE plpgsql;

CREATE TRIGGER generate_id BEFORE INSERT ON ip_scope
    FOR EACH ROW EXECUTE PROCEDURE generate_id();