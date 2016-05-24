<?php
  use Symfony\Component\HttpFoundation\Request;

  $app->get('/autocomplete/occupation/', function (Request $request) use ($app) {
    $query = $app->escape($request->get('query'));

    $Occupation = new Occupation($app['db']);

    return $app->json([
      'query' => $query,
      'suggestions' => $Occupation->autocompleteOccupation($query)
    ]);
  })
    ->bind('autocomplete.occupation');
