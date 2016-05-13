<?php
  $app->get('/downloads', function() use($app, $userLogged) {
    return $app['twig']->render('downloads.twig', [
      'page' => 'downloads',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('downloads');
