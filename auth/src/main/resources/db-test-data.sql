INSERT INTO role
(role_id, name)
VALUES
    (1, 'USER'),
    (2, 'MODERATOR'),
    (3, 'ADMIN');

INSERT INTO status
(status_id, name)
VALUES
    (1, 'NOT_CONFIRMED'),
    (2, 'CONFIRMED'),
    (3, 'BANNED'),
    (4, 'DELETED');

INSERT INTO authentication_info
(user_id, email, is_active, password_hash, refresh_token, status_status_id)
VALUES
    (1, 'email@email.com', true, '1111111!', '', 2);

INSERT INTO authorization_info
(user_id, access_token)
VALUES
    (1, 'access_token');

INSERT INTO authentication_info_roles
(authentication_info_user_id, roles_role_id)
VALUES
    (1, 2)
