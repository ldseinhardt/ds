<?php
  use Symfony\Component\HttpFoundation\Request;
  use Symfony\Component\Validator\Constraints as Assert;

  $app->match('/profile/edit/password/', function(Request $request) use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    $user = [
      'oldpassword' => $app->escape($request->get('oldpassword')),
      'password' => $app->escape($request->get('password')),
      'password2' => $app->escape($request->get('password2'))
    ];

    $response = NULL;

    if ($request->isMethod('POST')) {
      $constraint = new Assert\Collection([
        'oldpassword' => [
          new Assert\NotBlank([
            'message' => 'Informe sua senha atual.'
          ]),
          new Assert\Length([
            'min' => 4,
            'minMessage' => 'Sua senha atual deve possuir pelo menos {{ limit }} caracteres.'
          ])
        ],
        'password' => [
          new Assert\NotBlank([
            'message' => 'Informe sua nova senha.'
          ]),
          new Assert\Length([
            'min' => 4,
            'minMessage' => 'Sua nova senha deve possuir pelo menos {{ limit }} caracteres.'
          ]),
          new Assert\EqualTo([
            'value' => $user['password2'],
            'message' => 'As senhas informadas não são iguais.'
          ])
        ],
        'password2' => [
          new Assert\NotBlank([
            'message' => 'Informe a confirmação de sua nova senha.'
          ])
        ]
      ]);

      $errors = $app['validator']->validate($user, $constraint);

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
        $User = new User($app['db']);
        if ($User->auth($userLogged->email, $user['oldpassword'])) {
          $User->setUser($user)
            ->update();
          $response = (object) [
            'status' => true,
            'message' => 'Senha alterada com sucesso.'
          ];
        } else {
          $response = (object) [
            'status' => false,
            'message' => 'Senha atual inválida.'
          ];
        }
      }
    }

    return $app['twig']->render('profile_edit_password.twig', [
      'page' => 'profile_edit_password',
      'userLogged' => $userLogged,
      'user' => (object) $user,
      'response' => $response
    ]);
  }, 'GET|POST')
    ->bind('profile_edit_password');
