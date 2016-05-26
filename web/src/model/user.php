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

    public function isValidToken($token, $email = NULL) {
      if ($email) {
        $this->user['email'] = $email;
      }
      $today = date('Y-m-d H:i:s');
      $query = $this->db->fetchAssoc("
        SELECT
          email
        FROM
          users
        WHERE
          email = ? AND password_token = ? AND password_token_expires >= ? AND active = 1
      ", [
        $this->user['email'],
        $token,
        $today
      ]);
      return $query != NULL;
    }

    public function generatePasswordToken($email = NULL) {
      if ($email) {
        $this->user['email'] = $email;
      }
      $today = date('Y-m-d H:i:s');
      $token = hash('sha256', $this->secret . $this->user['email'] . time());
      $this->user['password_token'] = $token;
      $this->user['password_token_expires'] = date('Y-m-d H:i:s', strtotime($today .' +3 days'));
      $this->update();
      return $token;
    }

    public function emailExists($email = NULL) {
      if ($email) {
        $this->user['email'] = $email;
      }
      $query = $this->db->fetchAssoc("
        SELECT
          email
        FROM
          users
        WHERE
          email = ?
      ", [
        $this->user['email'],
      ]);
      return $query != NULL;
    }

    public function add($user = NULL) {
      $this->setUser($user);
      if (!$this->user['city_id']) {
        return NULL;
      }
      if (!$this->user['level_id']) {
        $this->user['level_id'] = 1;
      }
      if (!$this->user['active']) {
        $this->user['active'] = 1;
      }
      if ($this->user['education_id'] === '') {
        $this->user['education_id'] = NULL;
      }
      $this->user['created'] = date('Y-m-d H:i:s');
      $this->user['modified'] = date('Y-m-d H:i:s');
      return $this->db->insert('users', $this->user);
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

    public function getAll() {
      return $this->db->fetchAll("
        SELECT
          users.name,
          users.email,
          concat_ws(', ', cities.city, states.state, countries.country) AS location,
          levels.level,
          users.birthyear
        FROM
          users
          INNER JOIN cities
            ON (users.city_id = cities.id)
          INNER JOIN states
            ON (cities.state_id = states.id)
          INNER JOIN countries
            ON (states.country_id = countries.id)
          INNER JOIN levels
            ON (users.level_id = levels.id)
      ");
    }

    public function setLocation($location) {
      $Location = new Location($this->db, $location);
      $this->user['city_id'] = $Location->getLocationID();
      return $this;
    }

    public function setOccupation($occupation) {
      $Occupation = new Occupation($this->db, $occupation);
      $this->user['occupation_id'] = $Occupation->getOccupationID();
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

    private function hashPassword($password) {
      return hash('sha256', $this->secret . $password);
    }

  }
