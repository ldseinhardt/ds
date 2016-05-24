<?php
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
