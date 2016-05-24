<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/transactions/', function(Request $request) use($app, $userLogged) {
    if (!($userLogged && $userLogged->isAdmin)) {
      return $app->redirect('/');
    }

    if ($request->isMethod('POST')) {
      //...
    }

    return $app['twig']->render('transactions.twig', [
      'page' => 'transactions',
      'userLogged' => $userLogged
    ]);
  }, 'GET|POST')
    ->bind('transactions');
