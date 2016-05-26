<?php
  use Symfony\Component\HttpFoundation\Request;
  use Symfony\Component\Validator\Constraints as Assert;

  $app->match('/recover/password/', function(Request $request) use($app, $userLogged) {
    $user = [
      'email' => $app->escape($request->get('email')),
      'token' => $app->escape($request->get('token')),
      'password' => $app->escape($request->get('password')),
      'password2' => $app->escape($request->get('password2')),
    ];

    $response = NULL;

    if ($userLogged || !$user['email'] || !$user['token']) {
      return $app->redirect('/');
    }

    $User = new User($app['db']);

    if (!$User->isValidToken($user['token'], $user['email'])) {
      $response = (object) [
        'status' => false,
        'message' => "Token inválido.\nTente recuperar sua conta novamente ou entre em contato com o administrador."
      ];
    } else if ($request->isMethod('POST')) {
      $constraint = new Assert\Collection([
        'email' => [],
        'token' => [],
        'password' => [
          new Assert\NotBlank([
            'message' => 'Informe sua senha.'
          ]),
          new Assert\Length([
            'min' => 4,
            'minMessage' => 'Sua senha deve possuir pelo menos {{ limit }} caracteres.'
          ]),
          new Assert\EqualTo([
            'value' => $user['password2'],
            'message' => 'As senhas informadas não são iguais.'
          ])
        ],
        'password2' => [
          new Assert\NotBlank([
            'message' => 'Informe a confirmação de sua senha.'
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
        $user['password_token'] = '';
        $User->setUser($user)
          ->update();
        $response = (object) [
          'status' => true,
          'message' => 'Sua nova senha foi cadastrada com sucesso.'
        ];
      }
    }

    return $app['twig']->render('recover_password.twig', [
      'page' => 'recover_password',
      'userLogged' => NULL,
      'user' => $user,
      'response' => $response
    ]);
  }, 'GET|POST')
    ->bind('recover_password');
