<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->get('/profile/', function(Request $request) use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    $email = $app->escape($request->get('email'));

    $User = new User($app['db']);

    $usr = $userLogged->email;

    if ($userLogged->isAdmin && $email && $User->emailExists($email)) {
      $usr = $email;
    }

    $user = $User->getUser($usr);

    return $app['twig']->render('profile_view.twig', [
      'page' => 'profile_view',
      'userLogged' => $userLogged,
      'user' => $user,
      'email' => $email
    ]);
  })
    ->bind('profile_view');
