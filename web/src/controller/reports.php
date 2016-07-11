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

    $Transaction = new Transaction($app['db']);

    return $app['twig']->render('reports.twig', [
      'page' => 'reports',
      'userLogged' => $userLogged,
      'categories' => $Transaction->getCategories()
    ]);
  })
    ->bind('reports');

  $app->match('/reports/{report}.json', function($report, Request $request) use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    $File = new File($app['db']);

    if (!($File->hasValidUpload($userLogged->email) || $userLogged->isAdmin)) {
      return $app->redirect('/files/?message=' . urlencode('Por favor, envie seus dados para possuir acesso aos relatórios.'));
    }

    $Transaction = new Transaction($app['db']);

    $filters = [
      'date_initial' => $app->escape($request->get('date_initial')),
      'date_final' => $app->escape($request->get('date_final')),
      'category_id' => $app->escape($request->get('category_id')),
      'type_id' => $app->escape($request->get('type_id')),
      'location' => $app->escape($request->get('location')),
      'birthyear' => $app->escape($request->get('birthyear')),
      'occupation' => $app->escape($request->get('occupation')),
      'education_id' => $app->escape($request->get('education_id'))
    ];

    $filters2 = [
      'user' => $userLogged->email,
      'date_initial' => $app->escape($request->get('date_initial')),
      'date_final' => $app->escape($request->get('date_final')),
      'category_id' => $app->escape($request->get('category_id')),
      'type_id' => $app->escape($request->get('type_id')),
      'location' => $app->escape($request->get('location')),
      'birthyear' => $app->escape($request->get('birthyear')),
      'occupation' => $app->escape($request->get('occupation')),
      'education_id' => $app->escape($request->get('education_id'))
    ];

    $data = NULL;

    switch ($report) {
      case 'expense-vs-income':
        $data = $Transaction->getReportExpenseVsIncome($filters);
        break;
      case 'my-expense-vs-income':
        $data = $Transaction->getReportExpenseVsIncome($filters2);
        break;
      case 'transactions-per-day':
        $data = $Transaction->getReportTransactionsPerDay($filters);
        break;
      case 'my-transactions-per-day':
        $data = $Transaction->getReportTransactionsPerDay($filters2);
        break;
      case 'expenses-by-category':
        $data = $Transaction->getReportExpensesByCategory($filters);
        break;
      case 'my-expenses-by-category':
        $data = $Transaction->getReportExpensesByCategory($filters2);
        break;
      case 'incomes-by-category':
        $data = $Transaction->getReportIncomesByCategory($filters);
        break;
      case 'my-incomes-by-category':
        $data = $Transaction->getReportIncomesByCategory($filters2);
        break;
      // outros relatórios ...
    }

    return $app->json($data);
  }, 'GET|POST')
    ->bind('reports.types.json');
