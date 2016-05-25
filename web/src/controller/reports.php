<?php
  use Symfony\Component\HttpFoundation\Request;

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

  $app->get('/reports/types.json', function(Request $request) use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    $File = new File($app['db']);

    if (!($File->hasValidUpload($userLogged->email) || $userLogged->isAdmin)) {
      return $app->redirect('/files/?message=' . urlencode('Por favor, envie seus dados para possuir acesso aos relatórios.'));
    }

    $Transaction = new Transaction($app['db']);

    return $app->json($Transaction->getReportTypes());
  })
    ->bind('reports.types.json');
