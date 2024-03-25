INSERT INTO users (firstname, lastname, email, username, password)
VALUES ('Server', 'Admin', 'serveradmin@domain.com', 'admin', '$2a$10$a4pxwFAV23NzBOZp1nOdG.t.vZ34lmLkccV.HBR.zLeg3K.Gck11S');

SET @userId = (SELECT user_id FROM users WHERE username = 'admin');

INSERT INTO user_role_junction (user_id, role_id)
SELECT @userId, role_id FROM roles;