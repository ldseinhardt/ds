<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->get('/profile/', function(Request $request) use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    return $app['twig']->render('profile_view.twig', [
      'page' => 'profile_view',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('profile_view');
