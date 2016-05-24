<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/contact/', function(Request $request) use($app, $userLogged, $mailer) {
    $response = NULL;

    if ($request->isMethod('POST')) {
      $name = $app->escape($request->get('name'));
      $email = $app->escape($request->get('email'));
      $subject = $app->escape($request->get('subject'));
      $message = $app->escape($request->get('message'));

      try {
        $mail = Swift_Message::newInstance()
          ->setSubject($subject)
          ->setFrom($mailer->sender)
          ->setTo($mailer->receiver)
          ->setReplyTo([$email => $name])
          ->setBody($message);

        $app['mailer']->send($mail);

        $response = (object) [
          'message' => 'Mensagem enviada com sucesso.',
          'status' => true
        ];
      } catch (Exception $e) {
        $response = (object) [
          'message' => 'Houve um erro ao enviar sua mensagem de contato.',
          'status' => false
        ];
      }
    }

    return $app['twig']->render('contact.twig', [
      'page' => 'contact',
      'userLogged' => $userLogged,
      'response' => $response
    ]);
  }, 'GET|POST')
    ->bind('contact');
