INSERT INTO MEMBER (id, email, firstname, lastname, password_hash, is_admin)
VALUES ('9135f12e-1b66-4ee6-bbae-df37303cc154', 'admin@test.ch', 'Admin', 'User', '$2a$10$aDD6I9Ej5.W8busvlsdPx.JvMWyJX8cOeOfVb.3q73KH2swww/N9C', true); -- Password: password1234
INSERT INTO MEMBER (id, email, firstname, lastname, password_hash, is_admin)
VALUES ('9135f11e-1b66-4ee6-bbae-df37303cc154', 'user@test.ch', 'User', 'Account', '$2a$10$aDD6I9Ej5.W8busvlsdPx.JvMWyJX8cOeOfVb.3q73KH2swww/N9C', false); -- Password: password1234

INSERT INTO BOOKING (id, member, date, time, status)
VALUES ('413e2297-b84b-42ef-97ed-16a8a9d1d671', '9135f12e-1b66-4ee6-bbae-df37303cc154', '2022-09-22', 'MORNING', 'APPROVED'),
       ('b8160463-01a0-4c7a-bd46-5b3716dbe4c6', '9135f12e-1b66-4ee6-bbae-df37303cc154', '2022-09-25', 'DAY', 'DECLINED'),
       ('3c13c533-fbac-4881-b94d-f95cb2ef16c8', '9135f11e-1b66-4ee6-bbae-df37303cc154', '2022-10-08', 'DAY', 'ORDERED');