<?php
  use Symfony\Component\HttpFoundation\Request;
  use Symfony\Component\Validator\Constraints as Assert;

  $app->match('/register/', function(Request $request) use($app, $userLogged) {
    if ($userLogged) {
      return $app->redirect('/');
    }

    $user = [
      'name' => $app->escape($request->get('name')),
      'email' => $app->escape($request->get('email')),
      'password' => $app->escape($request->get('password')),
      'password2' => $app->escape($request->get('password2')),
      'location' => $app->escape($request->get('location')),
      'birthyear' => $app->escape($request->get('birthyear')),
      'occupation' => $app->escape($request->get('occupation')),
      'education_id' => $app->escape($request->get('education'))
    ];

    $response = NULL;

    if ($request->isMethod('POST')) {
      $constraint = new Assert\Collection([
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
        'email' => [
          new Assert\NotBlank([
            'message' => 'Informe seu email.'
          ]),
          new Assert\Email([
            'message' => 'Email inválido.'
          ])
        ],
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
        $User = new User($app['db'], $user);
        $User->setLocation($user['location']);
        if ($user['occupation']) {
          $User->setOccupation($user['occupation']);
        }
        if ($User->emailExists()) {
          $response = (object) [
            'status' => false,
            'message' => 'Este email já foi cadastrado.'
          ];
        } else {
          if ($User->add()) {
            $response = (object) [
              'status' => true,
              'message' => 'Cadastro realizado com sucesso.'
            ];
          } else {
            $response = (object) [
              'status' => false,
              'message' => 'Houve algum erro ao cadastrar seus dados.'
            ];
          }
        }
      }
    }

    return $app['twig']->render('register.twig', [
      'page' => 'register',
      'userLogged' => NULL,
      'user' => (object) $user,
      'response' => $response
    ]);
  }, 'GET|POST')
    ->bind('register');
