<?php

  require_once __DIR__ . '/../vendor/autoload.php';

  use Silex\Application;
  use Silex\Provider\DoctrineServiceProvider;
  use Silex\Provider\TwigServiceProvider;
  use Symfony\Component\HttpFoundation\Request;

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

  // test
  $userLogged = (object) [
    'email' => 'ldseinhardt@gmail.com',
    'first_name' => 'Luan',
    'last_name' => 'Einhardt',
    'full_name' => 'Luan Einhardt',    
  ];

  $app->get('/', function() use($app, $userLogged) {
    return $app['twig']->render('home.twig', [
      'page' => 'home',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('home');

  $app->get('/downloads', function() use($app, $userLogged) {
    return $app['twig']->render('downloads.twig', [
      'page' => 'downloads',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('downloads');

  $app->get('/contact', function() use($app, $userLogged) {
    return $app['twig']->render('contact.twig', [
      'page' => 'contact',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('contact');

  $app->get('/files', function() use($app, $userLogged) {
    return $app['twig']->render('files.twig', [
      'page' => 'files',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('files');

  $app->get('/reports', function() use($app, $userLogged) {
    return $app['twig']->render('reports.twig', [
      'page' => 'reports',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('reports');

  $app->match('/login/', function(Request $request) use($app, $userLogged) {
    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      $password = $app->escape($request->get('password'));
      
      //...
    }

    return $app['twig']->render('login.twig', [
      'page' => 'login',
      'userLogged' => null
    ]);
  }, 'GET|POST')
    ->bind('login');

  $app->get('/logout/', function(Request $request) use($app, $userLogged) {
    //...

    return $app->redirect('/login/');
  })
    ->bind('logout');

  $app->match('/register/', function(Request $request) use($app, $userLogged) {
    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      $password = $app->escape($request->get('password'));
      
      //...
    }

    return $app['twig']->render('register.twig', [
      'page' => 'register',
      'userLogged' => null
    ]);
  }, 'GET|POST')
    ->bind('register');

  $app->match('/recover/', function(Request $request) use($app, $userLogged) {
    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      
      //...
    }

    return $app['twig']->render('recover.twig', [
      'page' => 'recover',
      'userLogged' => null
    ]);
  }, 'GET|POST')
    ->bind('recover');

  $app->match('/recover/password/', function(Request $request) use($app, $userLogged) {
    $token = $app->escape($request->get('token'));

    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      
      //...
    }

    return $app['twig']->render('recover_password.twig', [
      'page' => 'recover_password',
      'userLogged' => null,
      'token' => $token  
    ]);
  }, 'GET|POST')
    ->bind('recover_password');

  $app->get('/profile/', function(Request $request) use($app, $userLogged) {
    //...

    return $app['twig']->render('profile_view.twig', [
      'page' => 'profile_view',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('profile_view');

  $app->match('/profile/edit/', function(Request $request) use($app, $userLogged) {
    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      
      //...
    }

    return $app['twig']->render('profile_edit.twig', [
      'page' => 'profile_edit',
      'userLogged' => $userLogged
    ]);
  }, 'GET|POST')
    ->bind('profile_edit');

  $app->match('/upload/', function(Request $request) use($app, $userLogged) {
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

    return $app['twig']->render('upload.twig', [
      'page' => 'upload',
      'userLogged' => $userLogged
    ]);
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
