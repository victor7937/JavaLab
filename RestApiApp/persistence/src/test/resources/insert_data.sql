INSERT INTO gift_certificate (id, name, description, price, duration, create_date, last_update_date) VALUES (1,'lessons certificate','lessons certificate for 2 lessons',20.00,2,'2021-06-08 14:28:20','2021-06-08 15:11:52');
INSERT INTO gift_certificate (id, name, description, price, duration, create_date, last_update_date) VALUES (2,'remote lessons certificate','lessons certificate for 3 lessons',23.00,3,'2021-06-08 14:28:20','2021-06-08 15:05:17');

INSERT INTO tag (id, name) VALUES (1,'study');
INSERT INTO tag (id, name) VALUES (2,'online');

INSERT INTO m2m_certificate_tag (cert_id, tag_id) VALUES (1,1);
INSERT INTO m2m_certificate_tag (cert_id, tag_id) VALUES (2,1);
INSERT INTO m2m_certificate_tag (cert_id, tag_id) VALUES (2,2);


