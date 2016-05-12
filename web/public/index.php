<?php

  require_once __DIR__ . '/../vendor/autoload.php';

  use Symfony\Component\HttpFoundation\Request;

  $app = new Silex\Application();

  $app->register(new Silex\Provider\TwigServiceProvider(), [
    'twig.options' => [
      'cache' => __DIR__ . '/../cache/twig'
    ],
	  'twig.path' => __DIR__ . '/../views'
  ]);

  $app->register(new Silex\Provider\DoctrineServiceProvider(), [
  	'db.options' => [
  		'driver'   => 'pdo_mysql',
      'charset'  => 'utf8',
  		'host'     => 'localhost',
  		'dbname'   => 'c9',
  		'username' => 'ldseinhardt',
  		'password' => ''
  	]
  ]);

  $app->get('/', function() use($app) {
    return $app['twig']->render('home.twig');
  })
    ->bind('home');

  $app->match('/login/', function(Request $request) use($app) {
    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      $password = $app->escape($request->get('password'));
      
      //...
    }

    return $app['twig']->render('login.twig');
  }, 'GET|POST')
    ->bind('login');

  $app->get('/logout/', function(Request $request) use($app) {
    //...

    return $app->redirect('/login/');
  })
    ->bind('logout');

  $app->match('/register/', function(Request $request) use($app) {
    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      $password = $app->escape($request->get('password'));
      
      //...
    }

    return $app['twig']->render('login.twig');
  }, 'GET|POST')
    ->bind('register');

  $app->match('/recover/', function(Request $request) use($app) {
    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      
      //...
    }

    return $app['twig']->render('recover.twig');
  }, 'GET|POST')
    ->bind('recover');

  $app->match('/recover/password/', function(Request $request) use($app) {
    $token = $app->escape($request->get('token'));

    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      
      //...
    }

    return $app['twig']->render('recover_password.twig', [
      'token' => $token  
    ]);
  }, 'GET|POST')
    ->bind('recover_password');

  $app->get('/profile/', function(Request $request) use($app) {
    //...

    return $app['twig']->render('profile_view.twig');
  })
    ->bind('profile_view');

  $app->match('/profile/edit/', function(Request $request) use($app) {
    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      
      //...
    }

    return $app['twig']->render('profile_edit.twig');
  }, 'GET|POST')
    ->bind('profile_edit');

  $app->match('/upload/', function(Request $request) use($app) {
    if ($request->isMethod('POST')) {
      $file = $request->files->get('data');      

      if ($file == null) {
        return false;
      }

      //$filename = $file->getClientOriginalName();
      
      $filename = md5(time()) . '.json';

      $path = __DIR__ . '/uploads/';

      $file->move($path, $filename);

      //...

      return $app->json([
        'filename' => $filename
      ]);
    }

    return $app['twig']->render('upload.twig');
  }, 'GET|POST')
    ->bind('upload');

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

    if (pathinfo($filename, PATHINFO_EXTENSION) === 'css') {
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
