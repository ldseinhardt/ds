<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/profile/edit/', function(Request $request) use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));

      //...
    }

    return $app['twig']->render('profile_edit.twig', [
      'page' => 'profile_edit',
      'userLogged' => $userLogged
    ]);
  }, 'GET|POST')
    ->bind('profile_edit');
