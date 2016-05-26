<?php
  define('APP_DIR', __DIR__ . '/..');
  define('SRC_DIR', APP_DIR . '/src');
  define('WWW_ROOT', dirname(__FILE__));

  require_once APP_DIR . '/vendor/autoload.php';
  require_once SRC_DIR . '/settings.php';

  use Silex\Application;
  use Silex\Provider\HttpCacheServiceProvider;
  use Silex\Provider\TwigServiceProvider;
  use Silex\Provider\DoctrineServiceProvider;
  use Silex\Provider\SessionServiceProvider;
  use Silex\Provider\SwiftmailerServiceProvider;
  use Silex\Provider\ValidatorServiceProvider;

  $app = new Application();

  $app->register(new HttpCacheServiceProvider(), [
    'http_cache.cache_dir' => APP_DIR . '/temp/cache/http_cache/',
    'http_cache.esi'       => NULL
  ]);

  $app->register(new TwigServiceProvider(), [
    'twig.options' => [
      'cache' => APP_DIR . '/temp/cache/twig/'
    ],
    'twig.path' => SRC_DIR . '/view/'
  ]);

  $app->register(new DoctrineServiceProvider(), [
    'db.options' => $db_options
  ]);

  $app->register(new Silex\Provider\SessionServiceProvider(), [
    'session.storage.options' => [
      'cookie_lifetime' => 31536000
    ]
  ]);

  $app->register(new SwiftmailerServiceProvider(), [
    'swiftmailer.options' => $mailer_options
  ]);

  $app->register(new ValidatorServiceProvider());

  require_once SRC_DIR . '/model.php';

  $user = $app['session']->get('user');
  $userLogged = NULL;

  if ($user && $user->email) {
    $User = new User($app['db']);
    $userLogged = $User->getUser($user->email);
    if ($userLogged) {
      $userLogged->first_name = $userLogged->name;
      $userLogged->last_name = '';
      $userLogged->isAdmin = $userLogged->level_id == 2;
      $names = explode(' ', $userLogged->name);
      if (count($names) > 1) {
        $userLogged->last_name = $names[count($names) - 1];
      }
    }
  }

  require_once SRC_DIR . '/controller.php';

  $app->get('/{type}/{file}', function($type, $file) use($app) {
    switch ($type) {
      case 'assets':
        $path = '/assets/';
        break;
      case 'vendor':
        $path = '/../bower_components/';
        break;
      default:
        $path = '/';
    }

    $filename = WWW_ROOT . $path . $file;

    if (!file_exists($filename)) {
      return $app->abort(404);
    }

    $headers = [];

    switch (pathinfo($filename, PATHINFO_EXTENSION)) {
      case 'css':
        $headers['content-type'] = 'text/css; charset=utf-8';
        break;
      case 'js':
        $headers['content-type'] = 'application/javascript; charset=utf-8';
        break;
      case 'json':
        $headers['content-type'] = 'application/json; charset=utf-8';
        break;
      case 'txt':
        $headers['content-type'] = 'text/plain; charset=utf-8';
        break;
    }

    return $app->sendFile($filename, 200, $headers);
  })
    ->assert('type', 'files|assets|vendor')
    ->assert('file', '.+')
    ->value('type', 'files')
    ->bind('file');

  $app->error(function(Exception $e, $code) use($app) {
    if (!$app['debug']) {
      return $app->redirect('/');
    }
  });

  $app['debug'] = $debug;

  if ($app['debug']) {
    $app->run();
  } else {
    $app['http_cache']->run();
  }
