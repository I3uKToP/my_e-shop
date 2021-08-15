INSERT INTO `users` (`username`, `password`)
    VALUE   ('super', '$2a$10$AmtLG5yxullDKJeJ6F6XrOZby5ij6jzDK0nVqlgysJ.IPNQe1s1D6'),
        ('admin', '$2a$10$KPIlK2pB/r0mxC0vZe0YCu07iWokpZs70fwacfW23zAW6A5AOxB5S');
GO

INSERT INTO `roles` (`name`)
    VALUE ('ROLE_SUPERADMIN'), ('ROLE_ADMIN');
GO

INSERT INTO `user_roles`(`user_id`, `role_id`)
SELECT (SELECT id FROM `users` WHERE `username` = 'super'), (SELECT id FROM `roles` WHERE `name` = 'ROLE_SUPERADMIN')
UNION ALL
SELECT (SELECT id FROM `users` WHERE `username` = 'admin'), (SELECT id FROM `roles` WHERE `name` = 'ROLE_ADMIN');
GO