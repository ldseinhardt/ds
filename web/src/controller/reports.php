<?php
  $app->get('/reports/', function() use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    return $app['twig']->render('reports.twig', [
      'page' => 'reports',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('reports');
