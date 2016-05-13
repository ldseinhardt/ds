<?php
  $app->get('/logout/', function() use($app, $userLogged) {
    return $app->redirect('/login/');
  })
    ->bind('logout');
