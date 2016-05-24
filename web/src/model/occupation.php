<?php
  class Occupation {

    private $db;

    private $occupation;

    function __construct($db, $occupation = NULL) {
      $this->db = $db;

      $this->setOccupation($occupation);
    }

    public function setOccupation($occupation) {
      $this->occupation = $occupation;
      return $this;
    }

    public function getOccupationID($occupation = NULL) {
      if ($occupation) {
        $this->setOccupation($occupation);
      }

      $occupationID = $this->searchOccupation($this->occupation);

      if ($occupationID) {
        return $occupationID;
      }

      return $this->addOccupation($this->occupation);
    }

    public function autocompleteOccupation($query) {
      return $this->db->fetchAll("
        SELECT DISTINCT
          occupation AS value
        FROM
          occupations
        WHERE occupation LIKE ?
        LIMIT 10
      ", ["%{$query}%"]);
    }

    private function addOccupation($occupation) {
      $this->db->insert('occupations', [
        'occupation' => $occupation
      ]);
      return $this->db->lastInsertId();
    }

    private function searchOccupation($occupation) {
      $query = $this->db->fetchAssoc("
        SELECT
          id
        FROM
          occupations
        WHERE
          occupation = ?
      ", [$occupation]);
      return $query ? $query['id'] : NULL;
    }

  }
