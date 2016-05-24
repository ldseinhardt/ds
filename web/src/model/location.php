<?php
  class Location {

    private $db;

    private $location;

    function __construct($db, $location = NULL) {
      $this->db = $db;

      $this->setLocation($location);
    }

    public function setLocation($location) {
      $this->location = $location;
      return $this;
    }

    public function getLocationID($location = NULL) {
      if ($location) {
        $this->setLocation($location);
      }

      if (!$this->location) {
        return 0;
      }

      $locations = explode(', ', $this->location);

      if (count($locations) !== 3) {
        return 0;
      }

      $country = $locations[2];
      $state = $locations[1];
      $city = $locations[0];

      $locationID = $this->searchFullLocation($this->location);

      if ($locationID) {
        return $locationID;
      }

      $countryID = $this->searchCountry($country);

      if (!$countryID) {
        $countryID = $this->addCountry($country);
      }

      $stateID = $this->searchState($countryID, $state);

      if (!$stateID) {
        $stateID = $this->addState($countryID, $state);
      }

      $locationID = $this->searchCity($stateID, $city);

      if (!$locationID) {
        $locationID = $this->addCity($stateID, $city);
      }

      return $locationID;
    }

    public function autocompleteLocation($query) {
      return $this->db->fetchAll("
        SELECT DISTINCT
          concat_ws(', ', city, state, country) AS value
        FROM
          cities
            LEFT JOIN states ON (cities.state_id = states.id)
            LEFT JOIN countries ON (states.country_id = countries.id)
        WHERE
          concat_ws(', ', city, state, country) LIKE ?
        LIMIT 10
      ", ["{$query}%"]);
    }

    private function searchFullLocation($location) {
      $query = $this->db->fetchAssoc("
        SELECT
          cities.id
        FROM
          cities
            LEFT JOIN states ON (cities.state_id = states.id)
            LEFT JOIN countries ON (states.country_id = countries.id)
        WHERE
          concat_ws(', ', city, state, country) = ?
        LIMIT 1
      ", [$location]);
      return $query ? $query['id'] : 0;
    }

    private function searchCountry($country) {
      $query = $this->db->fetchAssoc("
        SELECT
          id
        FROM
          countries
        WHERE
          country = ?
      ", [$country]);
      return $query ? $query['id'] : 0;
    }

    private function searchState($id, $state) {
      $query = $this->db->fetchAssoc("
        SELECT
          id
        FROM
          states
        WHERE
          country_id = ? AND state = ?
      ", [(int) $id, $state]);
      return $query ? $query['id'] : 0;
    }

    private function searchCity($id, $city) {
      $query = $this->db->fetchAssoc("
        SELECT
          id
        FROM
          cities
        WHERE
          state_id = ? AND city = ?
      ", [(int) $id, $city]);
      return $query ? $query['id'] : 0;
    }

    private function addCountry($country) {
      $this->db->insert('countries', [
        'country' => $country
      ]);
      return $this->db->lastInsertId();
    }

    private function addState($id, $state) {
      $this->db->insert('states', [
        'country_id' => (int) $id,
        'state' => $state
      ]);
      return $this->db->lastInsertId();
    }

    private function addCity($id, $city) {
      $this->db->insert('cities', [
        'state_id' => (int) $id,
        'city' => $city
      ]);
      return $this->db->lastInsertId();
    }

  }
