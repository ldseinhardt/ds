<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/transactions/', function(Request $request) use($app, $userLogged) {
    if (!($userLogged && $userLogged->isAdmin)) {
      return $app->redirect('/');
    }

    if ($request->isMethod('POST')) {
      $action = $app->escape($request->get('action'));
      switch ($action) {
        case 'Remover Transações':
          $Transaction = new Transaction($app['db']);
          $Transaction->clearTransactions();
          break;
      }
    }

    return $app['twig']->render('transactions.twig', [
      'page' => 'transactions',
      'userLogged' => $userLogged
    ]);
  }, 'GET|POST')
    ->bind('transactions');
