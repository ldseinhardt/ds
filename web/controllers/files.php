<?php
  $app->get('/files/', function() use($app, $userLogged) {
    if ($userLogged == null) {
      return $app->redirect('/login/');
    }

    return $app['twig']->render('files.twig', [
      'page' => 'files',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('files');
