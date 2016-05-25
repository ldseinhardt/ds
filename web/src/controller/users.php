<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->get('/users/', function() use($app, $userLogged) {
    if (!($userLogged && $userLogged->isAdmin)) {
      return $app->redirect('/');
    }

    return $app['twig']->render('users.twig', [
      'page' => 'users',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('users');

  $app->get('/users.json', function(Request $request) use($app, $userLogged) {
    if (!($userLogged && $userLogged->isAdmin)) {
      return $app->redirect('/');
    }

    $User = new User($app['db']);

    return $app->json($User->getAll());
  })
    ->bind('users.json');
