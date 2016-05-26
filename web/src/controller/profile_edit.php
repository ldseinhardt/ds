<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/profile/edit/', function(Request $request) use($app, $userLogged) {
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

    $response = NULL;

    if ($request->isMethod('POST')) {
      $user_data = [
        'name' => $app->escape($request->get('name')),
        'password' => $app->escape($request->get('password')),
        'password2' => $app->escape($request->get('password2')),
        'location' => $app->escape($request->get('location')),
        'birthyear' => $app->escape($request->get('birthyear')),
        'occupation' => $app->escape($request->get('occupation')),
        'education_id' => $app->escape($request->get('education'))
      ];

      //...
    }

    return $app['twig']->render('profile_edit.twig', [
      'page' => 'profile_edit',
      'userLogged' => $userLogged,
      'user' => $user,
      'email' => $email,
      'response' => $response
    ]);
  }, 'GET|POST')
    ->bind('profile_edit');
