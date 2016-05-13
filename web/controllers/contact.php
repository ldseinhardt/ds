<?php
  $app->get('/contact', function() use($app, $userLogged) {
    return $app['twig']->render('contact.twig', [
      'page' => 'contact',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('contact');
