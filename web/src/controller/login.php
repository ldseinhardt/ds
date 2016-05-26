<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/login/', function(Request $request) use($app, $userLogged) {
    if ($userLogged) {
      return $app->redirect('/');
    }

    $email = $app->escape($request->get('email'));
    $password = $app->escape($request->get('password'));

    $response = NULL;

    if ($request->isMethod('POST')) {
      $User = new User($app['db']);
      $user = $User->auth($email, $password);

      if ($user) {
        $app['session']->set('user', $user);
        return $app->redirect('/');
      }

      $response = (object) [
        'status' => false,
        'message' => 'Login inválido.'
      ];
    }

    return $app['twig']->render('login.twig', [
      'page' => 'login',
      'userLogged' => NULL,
      'email' => $email,
      'password' => $password,
      'response' => $response
    ]);
  }, 'GET|POST')
    ->bind('login');
