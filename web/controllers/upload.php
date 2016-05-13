<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/upload/', function(Request $request) use($app, $userLogged) {
    if ($userLogged == null) {
      return $app->redirect('/login/');
    }

    if ($request->isMethod('POST')) {
      $file = $request->files->get('data');

      if ($file == null) {
        return false;
      }

      //$filename = $file->getClientOriginalName();

      $filename = md5(time()) . '.json';

      $path = __DIR__ . '/../public/uploads/';

      $file->move($path, $filename);

      //...

      return $app->json([
        'filename' => $filename
      ]);
    }

    return $app['twig']->render('upload.twig', [
      'page' => 'upload',
      'userLogged' => $userLogged
    ]);
  }, 'GET|POST')
    ->bind('upload');
