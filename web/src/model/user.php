<?php
  class User {

    private $db;
    private $secret;

    private $user;

    function __construct($db, $user = NULL) {
      $this->db = $db;
      $this->secret = '1fe2141e1fc608439cc1f217222971f371517f6e6f73c903e77182f3dfe88446';

      $this->user = [
        'email'                  => NULL,
        'name'                   => NULL,
        'password'               => NULL,
        'city_id'                => NULL,
        'level_id'               => NULL,
        'active'                 => NULL,
        'password_token'         => NULL,
        'password_token_expires' => NULL,
        'birthyear'              => NULL,
        'occupation_id'          => NULL,
        'education_id'           => NULL,
        'created'                => NULL,
        'modified'               => NULL
      ];

      $this->setUser($user);
    }

    public function setUser($user) {
      if ($user) {
        foreach ($user as $key => $value) {
          if (array_key_exists($key, $this->user)) {
            if ($key === 'password') {
              $this->user[$key] = $this->hashPassword($value);
            } else {
              $this->user[$key] = $value;
            }
          }
        }
      }
      return $this;
    }

    public function auth($email = NULL, $password = NULL) {
      if ($email) {
        $this->user['email'] = $email;
      }
      if ($password) {
        $this->user['password'] = $this->hashPassword($password);
      }
      $query = $this->db->fetchAssoc("
        SELECT
          email
        FROM
          users
        WHERE
          email = ? AND password = ? AND active = 1
      ", [
        $this->user['email'],
        $this->user['password']
      ]);
      if ($query) {
        return (object) $query;
      }
      return NULL;
    }

    public function add($user = NULL) {
      $this->setUser($user);
      if (!($this->user['city_id'] && $this->user['level_id'])) {
        return NULL;
      }
      $this->user['created'] = date('Y-m-d H:i:s');
      $this->user['modified'] = date('Y-m-d H:i:s');
      $this->db->insert('users', $this->user);
      return $this->db->lastInsertId();
    }

    public function getUser($email = NULL) {
      if ($email) {
        $this->user['email'] = $email;
      }
      $query = $this->db->fetchAssoc("
        SELECT
          users.email,
          users.name,
          users.birthyear,
          users.level_id,
          users.education_id,
          concat_ws(', ', city, state, country) AS location
        FROM
          users
            LEFT JOIN cities ON (users.city_id = cities.id)
            LEFT JOIN states ON (cities.state_id = states.id)
            LEFT JOIN countries ON (states.country_id = countries.id)
        WHERE
          users.email = ?
      ", [$this->user['email']]);
      if ($query) {
        $user = (object) $query;
        return $user;
      }
      return NULL;
    }

    public function setLocation($location) {
      $Location = new Location($this->db, $location);
      $this->user['city_id'] = $Location->getLocationID();
      return $this;
    }

    public function autocompleteEmail($query) {
      return $this->db->fetchAll("
        SELECT DISTINCT
          email AS value
        FROM
          users
        WHERE email LIKE ?
        LIMIT 10
      ", ["{$query}%"]);
    }

    public function hashPassword($password) {
      return hash('sha256', $this->secret . $password);
    }

    public function update($user = NULL) {
      $this->setUser($user);

      $fields = array_filter($this->user, function($e) {
    		return $e !== NULL;
    	});
    	unset($fields['email']);

    	$values = array_values($fields);
    	$values[] = $this->user['email'];

    	$fields = implode(', ', array_map(function ($e) {
    		return $e . ' = ?';
    	}, array_keys($fields)));

      $this->db->executeUpdate("
        UPDATE
          users
        SET {$fields}
        WHERE
          email = ?
      ", $values);

      return $this;
    }

  }
