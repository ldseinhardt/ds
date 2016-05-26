<?php
  use Symfony\Component\HttpFoundation\Request;
  use Symfony\Component\Validator\Constraints as Assert;

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
        'location' => $app->escape($request->get('location')),
        'birthyear' => $app->escape($request->get('birthyear')),
        'occupation' => $app->escape($request->get('occupation')),
        'education_id' => $app->escape($request->get('education_id'))
      ];

      if ($userLogged->isAdmin) {
        $user_data['level_id'] = $app->escape($request->get('level_id'));
        $user_data['active'] = $app->escape($request->get('active'));
      }

      $user = (object) $user_data;

      $collection = [
        'name' => [
          new Assert\NotBlank([
            'message' => 'Informe seu nome.'
          ]),
          new Assert\Length([
            'min' => 5,
            'max' => 128,
            'minMessage' => 'Seu nome deve possuir pelo menos {{ limit }} caracteres.',
            'maxMessage' => 'Seu nome deve possuir no máximo {{ limit }} caracteres.'
          ])
        ],
        'location' => [
          new Assert\NotBlank([
            'message' => 'Informe sua localização (Cidade, Estado, País).'
          ]),
          new Assert\Length([
            'min' => 10,
            'max' => 128,
            'minMessage' => 'Sua localização deve possuir pelo menos {{ limit }} caracteres.',
            'maxMessage' => 'Sua localização deve possuir no máximo {{ limit }} caracteres.'
          ])
        ],
        'birthyear' => [],
        'occupation' => [
          new Assert\Length([
            'min' => 5,
            'max' => 64,
            'minMessage' => 'Sua ocupação deve possuir pelo menos {{ limit }} caracteres.',
            'maxMessage' => 'Sua ocupação deve possuir no máximo {{ limit }} caracteres.'
          ])
        ],
        'education_id' => []
      ];

      if ($userLogged->isAdmin) {
        $collection['level_id'] = [];
        $collection['active'] = [];
      }

      $constraint = new Assert\Collection($collection);

      $errors = $app['validator']->validate($user_data, $constraint);

      if (count($errors)) {
        $response = (object) [
          'status' => false,
          'message' => 'Houve algum erro ao validar seus dados.',
          'errors' => []
        ];
        if (count($errors) > 1) {
          $response->message = 'Houve alguns erros ao validar seus dados.';
        }
        foreach ($errors as $error) {
          $response->errors[] = $error->getMessage();
        }
      } else {
        $User->setLocation($user_data['location']);
        if ($user_data['occupation']) {
          $User->setOccupation($user_data['occupation']);
        } else {
          $user_data['occupation_id'] = 'NULL';
        }
        if (!$user_data['education_id']) {
          $user_data['education_id'] = 'NULL';
        }
        $User->setUser($user_data)
          ->update();
        $response = (object) [
          'status' => true,
          'message' => 'Perfil atualizado com sucesso.'
        ];
      }
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
