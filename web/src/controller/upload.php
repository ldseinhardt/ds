<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/upload/', function(Request $request) use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    if ($request->isMethod('POST')) {
      $file = $request->files->get('data');

      if (!$file) {
        return false;
      }

      $filename = md5($userLogged->email) . '_' . md5(time()) . '.json';

      $path = WWW_ROOT . '/uploads/';

      $file->move($path, $filename);

      try {
        $content = file_get_contents($path . $filename);
        $data = json_decode($content);
      } catch (Exception $e) {

      }

      if (!$data) {
        return false;
      }

      //inserir arquivo na tabela de arquivos

      $today = strtotime(date('Y-m-d'));
      $Transaction = new Transaction($app['db']);
      $Transaction->setUser($userLogged->email)
        ->removeAll();

      foreach ($data->accounts as $account) {
        foreach ($account->transactions as $transaction) {
          foreach ($transaction->payments as $payment) {
            if ($payment->concretized && strtotime(date($payment->date)) <= $today) {
              $Transaction->setCategory($transaction->category, $transaction->type)
                ->add([
                  'date' => $payment->date,
                  'value' => $payment->value
                ]);
            }
          }
        }
      }

      return $app->json([
        'filename' => $filename
      ]);
    }

    return $app['twig']->render('upload.twig', [
      'page' => 'upload',
      'userLogged' => $userLogged
    ]);
  }, 'GET|POST')
    ->bind('upload');
