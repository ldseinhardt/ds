<?php
  $app->get('/logout/', function() use($app) {
    $app['session']->remove('user');
    return $app->redirect('/login/');
  })
    ->bind('logout');
