-- Script para teste do banco de dados

USE oraculo;

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
