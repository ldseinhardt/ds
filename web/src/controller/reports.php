<?php
  $app->get('/reports/', function() use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    $File = new File($app['db']);

    if (!($File->hasValidUpload($userLogged->email) || $userLogged->isAdmin)) {
      return $app->redirect('/files/?message=' . urlencode('Por favor, envie seus dados para possuir acesso aos relatórios.'));
    }

    return $app['twig']->render('reports.twig', [
      'page' => 'reports',
      'userLogged' => $userLogged
    ]);
  })
    ->bind('reports');
