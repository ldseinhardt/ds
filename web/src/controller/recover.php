<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/recover/', function(Request $request) use($app, $userLogged) {
    if ($userLogged) {
      return $app->redirect('/');
    }

    if ($request->isMethod('POST')) {
      $email = $app->escape($request->get('email'));

      //...
    }

    return $app['twig']->render('recover.twig', [
      'page' => 'recover',
      'userLogged' => NULL
    ]);
  }, 'GET|POST')
    ->bind('recover');
