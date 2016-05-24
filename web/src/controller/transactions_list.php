<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->get('/transactions/list/', function(Request $request) use($app, $userLogged) {
    if (!($userLogged && $userLogged->isAdmin)) {
      return $app->redirect('/');
    }

    $Transaction = new Transaction($app['db']);

    return $app->json($Transaction->getAll());
  })
    ->bind('transactions_list');
