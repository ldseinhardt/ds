<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/recover/password/', function(Request $request) use($app, $userLogged) {
    if ($userLogged) {
      return $app->redirect('/');
    }

    $token = $app->escape($request->get('token'));

    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));

      //...
    }

    return $app['twig']->render('recover_password.twig', [
      'page' => 'recover_password',
      'userLogged' => NULL,
      'token' => $token
    ]);
  }, 'GET|POST')
    ->bind('recover_password');
