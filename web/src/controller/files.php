<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/files/', function(Request $request) use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    $message = $app->escape($request->get('message'));

    $response = NULL;

    if ($request->isMethod('POST')) {
      $file = $request->files->get('data');

      if ($file) {
        $filename = md5($userLogged->email) . '_' . md5(time()) . '.json';

        $path = WWW_ROOT . '/uploads/';

        $file->move($path, $filename);

        try {
          $content = file_get_contents($path . $filename);
          $data = json_decode($content);
        } catch (Exception $e) {

        }

        if ($data) {
          $File = new File($app['db']);
          $File->setUser($userLogged->email)
            ->add($filename);

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

          $message = NULL;

          $response = (object) [
            'status' => true,
            'message' => 'Obrigado por compartilhar suas finanças !!!'
          ];
        }
      }
    }

    return $app['twig']->render('files.twig', [
      'page' => 'files',
      'userLogged' => $userLogged,
      'message' => $message,
      'response' => $response,
    ]);
  }, 'GET|POST')
    ->bind('files');
