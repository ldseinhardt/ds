<?php
  $debug = true;

  $db_options = [
    'driver'   => 'pdo_mysql',
    'charset'  => 'utf8',
    'host'     => 'localhost',
    'dbname'   => 'oraculo',
    'username' => 'root',
    'password' => '11108239cc'
  ];

  /*
    Só irá funcionar localmente ou em um servidor com a porta 587 habilitada
  */
  $mailer_options = [
    'host'       => 'smtp.mail.yahoo.com',
    'port'       => 587,
    'username'   => 'oraculodamascada@yahoo.com',
    'password'   => 'Oraculo123',
    'encryption' => 'tls'
  ];

  $mailer = (object) [
    'sender' => [
      'oraculodamascada@yahoo.com' => 'Oráculo da Mascada'
    ],
    'receiver' => [
      'ldseinhardt@gmail.com' => 'Luan Einhardt'
    ]
  ];
