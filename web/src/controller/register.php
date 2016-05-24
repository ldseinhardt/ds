<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/register/', function(Request $request) use($app, $userLogged) {
    if ($userLogged) {
      return $app->redirect('/');
    }

    if ($request->isMethod('POST')) {
      $name = $app->escape($request->get('name'));
      $email = $app->escape($request->get('email'));
      $password = $app->escape($request->get('password'));
      $location = $app->escape($request->get('location'));
      $occupation = $app->escape($request->get('occupation'));
      $education = $app->escape($request->get('education'));

      //...
    }

    return $app['twig']->render('register.twig', [
      'page' => 'register',
      'userLogged' => NULL
    ]);
  }, 'GET|POST')
    ->bind('register');
