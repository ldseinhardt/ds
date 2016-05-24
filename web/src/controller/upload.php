<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->match('/upload/', function(Request $request) use($app, $userLogged) {
    if (!$userLogged) {
      return $app->redirect('/login/');
    }

    if ($request->isMethod('POST')) {
      $file = $request->files->get('data');

      if (!$file) {
        return false;
      }

      $filename = md5($userLogged->email) . '_' . md5(time()) . '.json';

      $path = WWW_ROOT . '/uploads/';

      $file->move($path, $filename);

      try {
        $content = file_get_contents($path . $filename);
        $data = json_decode($content);
      } catch (Exception $e) {

      }

      if (!$data) {
        return false;
      }

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
