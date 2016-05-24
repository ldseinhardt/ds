<?php
  class File {

    private $db;

    private $user;

    private $file;

    function __construct($db, $file = NULL) {
      $this->db = $db;

      $this->setFile($file);
    }

    public function setFile($file) {
      $this->file = $file;
      return $this;
    }

    public function setUser($user) {
      $this->user = $user;
      return $this;
    }

    public function add($file = NULL) {
      if ($file) {
        $this->file = $file;
      }
      $this->db->insert('files', [
        'file' => $this->file,
        'user_email' => $this->user,
        'date' => date('Y-m-d H:i:s')
      ]);
      return $this->db->lastInsertId();
    }

    public function hasValidUpload($user) {
      if ($user) {
        $this->user = $user;
      }
      $today = date('Y-m-d H:i:s');
      $u30d = date('Y-m-d H:i:s', strtotime($today .' -1 months'));
      $query = $this->db->fetchAssoc("
        SELECT
          COUNT(file) as count
        FROM
          files
        WHERE
          user_email = ? AND date >= ?
      ", [$this->user, $u30d]);
      return $query && $query['count'] > 0;
    }

    public function getAll($user) {
      if ($user) {
        $this->user = $user;
      }
      return $this->db->fetchAll("
        SELECT
          file,
          date
        FROM
          files
        WHERE
          user_email = ?
      ", [$this->user]);
    }

  }
