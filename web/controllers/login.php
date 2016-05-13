<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/login/', function(Request $request) use($app, $userLogged) {
    //teste
    $userLogged = null;

    if ($userLogged != null) {
      return $app->redirect('/');
    }

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
