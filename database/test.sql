-- Script para teste do banco de dados

USE oraculo;

-- Senha padrão é odm
INSERT INTO `users` (`email`, `name`, `password`, `city_id`, `birthyear`, `level_id`, `education_id`, `created`, `modified`) VALUES
  ('ldseinhardt@gmail.com', 'Luan Einhardt', 'fba638d141f79e872fcea2b28d96aef01f76598775ce256c8d98e9185325ee58', 4226, 1992, 1, 8, NOW(), NOW()),
  ('rodrigodroliveira@gmail.com', 'Iluminati', 'fba638d141f79e872fcea2b28d96aef01f76598775ce256c8d98e9185325ee58', 4226, 1981, 1, 8, NOW(), NOW());

INSERT INTO `transactions` (`user_email`, `category_id`, `date`, `value`) VALUES
  ('admin@oraculodamascada.com.br',  1, CURDATE(), 10.75),
  ('admin@oraculodamascada.com.br',  9, CURDATE(), 20),
  ('admin@oraculodamascada.com.br', 11, CURDATE(), 10),
  ('admin@oraculodamascada.com.br', 29, CURDATE(), 1000),
  ('ldseinhardt@gmail.com', 29, CURDATE(), 500),
  ('ldseinhardt@gmail.com',  1, CURDATE(), 10.75),
  ('ldseinhardt@gmail.com', 11, CURDATE(), 5);

SELECT
  `users`.`name` AS `user`,
  `transactions`.`id`,
  `transactions`.`date`,
  `categories`.`category`,
  `categories`.`type_id`
FROM
  `transactions`
  INNER JOIN `users`
    ON (`transactions`.`user_email` = `users`.`email`)
  INNER JOIN `categories`
    ON (`transactions`.`category_id` = `categories`.`id`)
;

DELETE FROM
  `transactions`
WHERE
  `user_email` = 'admin@oraculodamascada.com.br'
;

SELECT
  `users`.`name` AS `user`,
  `transactions`.`id`,
  `transactions`.`date`,
  `categories`.`category`,
  `categories`.`type_id`
FROM
  `transactions`
  INNER JOIN `users`
    ON (`transactions`.`user_email` = `users`.`email`)
  INNER JOIN `categories`
    ON (`transactions`.`category_id` = `categories`.`id`)
;
