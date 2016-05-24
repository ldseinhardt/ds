<?php
  define('CONTROLLER_DIR', SRC_DIR . '/controller');

  function controllers($path) {
    $list = [];
    $dir = new DirectoryIterator($path);
    foreach ($dir as $entry) {
      if ($entry->isDir() && !$entry->isDot()) {
        $list = array_merge($list, controllers($path . $entry . '/'));
      } else if (!$entry->isDir()) {
        $list[] = $path . $entry->getFilename();
      }
    }
    return $list;
  }

  $controllers = controllers(CONTROLLER_DIR . '/');

  foreach ($controllers as $controller) {
    require_once $controller;
  }
