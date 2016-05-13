<?php
  require_once __DIR__ . '/../vendor/autoload.php';

  use Silex\Application;
  use Silex\Provider\DoctrineServiceProvider;
  use Silex\Provider\TwigServiceProvider;

  $app = new Application();

  $app->register(new TwigServiceProvider(), [
    'twig.options' => [
      'cache' => __DIR__ . '/../cache/twig'
    ],
    'twig.path' => __DIR__ . '/../views'
  ]);

  $app->register(new DoctrineServiceProvider(), [
    'db.options' => [
      'driver'   => 'pdo_mysql',
      'charset'  => 'utf8',
      'host'     => 'localhost',
      'dbname'   => 'c9',
      'username' => 'ldseinhardt',
      'password' => ''
    ]
  ]);

  // teste
  $userLogged = (object) [
    'email' => 'ldseinhardt@gmail.com',
    'full_name' => 'Luan Einhardt',
    'nivel_id' => 1
  ];

  $userLogged->first_name = $userLogged->full_name;
  $userLogged->last_name = '';
  $userLogged->isAdmin = $userLogged->nivel_id === 1;

  $names = explode(' ', $userLogged->full_name);

  if (count($names) > 1) {
    $userLogged->last_name = $names[count($names) - 1];
  }

  $controllers = __DIR__ . '/../controllers';

  require_once $controllers . '/home.php';
  require_once $controllers . '/downloads.php';
  require_once $controllers . '/contact.php';
  require_once $controllers . '/files.php';
  require_once $controllers . '/reports.php';
  require_once $controllers . '/login.php';
  require_once $controllers . '/logout.php';
  require_once $controllers . '/register.php';
  require_once $controllers . '/recover.php';
  require_once $controllers . '/recover_password.php';
  require_once $controllers . '/profile_view.php';
  require_once $controllers . '/profile_edit.php';
  require_once $controllers . '/upload.php';
  require_once $controllers . '/users.php';

  $app->get('/{type}/{file}', function($type, $file) use($app) {
    $path = '/';

    if ($type === 'assets') {
      $path = '/../bower_components/';
    }

    $filename = __DIR__ . $path . $file;

    if (!file_exists($filename)) {
      return $app->abort(404);
    }

    $headers = [];

    switch (pathinfo($filename, PATHINFO_EXTENSION)) {
      case 'css':
        $headers['content-type'] = 'text/css';
    }

    return $app->sendFile($filename, 200, $headers);
  })
    ->assert('type', 'files|assets')
    ->assert('file', '.+')
    ->bind('file');

  $app->error(function(Exception $e, $code) use($app) {
    if (!$app['debug']) {
      return $app->redirect('/');
    }
  });

  $app['debug'] = true;

  $app->run();
