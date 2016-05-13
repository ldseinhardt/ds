<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/users/', function(Request $request) use($app, $userLogged) {
    if ($userLogged == null || !$userLogged->isAdmin) {
      return $app->redirect('/');
    }

    if ($request->isMethod('POST')) {
      //...
    }

    return $app['twig']->render('users.twig', [
      'page' => 'users',
      'userLogged' => $userLogged
    ]);
  }, 'GET|POST')
    ->bind('users');
