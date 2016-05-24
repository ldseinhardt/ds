<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->get('/files/list/', function(Request $request) use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    $File = new File($app['db']);

    return $app->json($File->getAll($userLogged->email));
  })
    ->bind('files_list');
