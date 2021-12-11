INSERT into account(id, date_created, is_active, date_modified) values
(1, now(), true, now()),
(2, now(), true, now()),
(3, now(), true, now())
ON CONFLICT (id) DO NOTHING;
