<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->get('/users/list/', function(Request $request) use($app, $userLogged) {
    if (!($userLogged && $userLogged->isAdmin)) {
      return $app->redirect('/');
    }

    $User = new User($app['db']);

    return $app->json($User->getAll());
  })
    ->bind('users_list');
