<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->get('/autocomplete/location/', function (Request $request) use ($app) {
    $query = $app->escape($request->get('query'));

    $Location = new Location($app['db']);

    return $app->json([
      'query' => $query,
      'suggestions' => $Location->autocompleteLocation($query)
    ]);
  })
    ->bind('autocomplete.location');
