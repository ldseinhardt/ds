<?php
  $app->get('/', function() use($app, $userLogged) {
    return $app['twig']->render('home.twig', [
      'page' => 'home',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('home');
