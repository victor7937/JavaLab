CREATE TABLE gift_certificate (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(70) NOT NULL,
    description text,
    price decimal(6,2) NOT NULL,
    duration int NOT NULL,
    create_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE m2m_certificate_tag (
       cert_id int NOT NULL,
       tag_id int NOT NULL,
       PRIMARY KEY (cert_id, tag_id)
);

CREATE TABLE tag(
        id int NOT NULL AUTO_INCREMENT,
        name varchar(30) NOT NULL,
        PRIMARY KEY (id),
        UNIQUE KEY tag_name_uindex (name)
);


ALTER TABLE m2m_certificate_tag ADD CONSTRAINT m2m_certificate_tag_gift_certificate_id_fk FOREIGN KEY (cert_id) REFERENCES gift_certificate(id)  ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE m2m_certificate_tag ADD CONSTRAINT m2m_certificate_tag_tag_id_fk FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE ON UPDATE CASCADE;


