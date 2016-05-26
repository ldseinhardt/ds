<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/recover/', function(Request $request) use($app, $userLogged, $mailer) {
    if ($userLogged) {
      return $app->redirect('/');
    }

    $email = $app->escape($request->get('email'));

    $response = NULL;

    if ($request->isMethod('POST')) {
      $User = new User($app['db']);
      if ($User->emailExists($email)) {
        $token = $User->generatePasswordToken();
        $user = $User->getUser();
        try {
          $mail = Swift_Message::newInstance()
            ->setSubject('Recuperação de conta')
            ->setFrom($mailer->sender)
            ->setTo([$user->email => $user->name])
            ->setReplyTo($mailer->receiver)
            ->setBody($app['twig']->render('recover_message.twig', [
              'user' => $user,
              'token' => $token
            ]), 'text/html');
          $app['mailer']->send($mail);
          $response = (object) [
            'status' => true,
            'message' => 'Consulte sua caixa de entrada e siga os passos recomendados.'
          ];
        } catch (Exception $e) {
          $response = (object) [
            'message' => 'Houve um erro com o servidor de emails.',
            'status' => false
          ];
        }
      } else {
        $response = (object) [
          'status' => false,
          'message' => 'Email não cadastrado.'
        ];
      }
    }

    return $app['twig']->render('recover.twig', [
      'page' => 'recover',
      'userLogged' => NULL,
      'email' => $email,
      'response' => $response
    ]);
  }, 'GET|POST')
    ->bind('recover');
