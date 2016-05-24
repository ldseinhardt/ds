<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/register/', function(Request $request) use($app, $userLogged) {
    if ($userLogged) {
      return $app->redirect('/');
    }

    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));
      $password = $app->escape($request->get('password'));

      //...
    }

    return $app['twig']->render('register.twig', [
      'page' => 'register',
      'userLogged' => NULL
    ]);
  }, 'GET|POST')
    ->bind('register');
