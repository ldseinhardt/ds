<?php
  class Transaction {

    private $db;

    private $transaction;

    function __construct($db, $transaction = NULL) {
      $this->db = $db;

      $this->transaction = [
        'id'          => NULL,
        'user_email'  => NULL,
        'category_id' => NULL,
        'date'        => NULL,
        'value'       => NULL
      ];

      $this->setTransaction($transaction);
    }

    public function setUser($user) {
      $this->transaction['user_email'] = $user;
      return $this;
    }

    public function setCategory($category, $type) {
      $this->transaction['category_id'] = $this->searchCategory($category, $type);
      return $this;
    }

    public function searchCategory($category, $type) {
      $query = $this->db->fetchAssoc("
        SELECT
          id
        FROM
          categories
        WHERE
          category = ? AND type_id = ?
      ", [
        $category,
        $this->getTransactionTypeId($type)
      ]);
      return $query ? $query['id'] : NULL;
    }

    public function getTransactionTypeId($type) {
      switch ($type) {
        case 'Expense':
        case 'Despesa':
          return 1;
        case 'Income':
        case 'Receita':
          return 2;
      }
      return $type;
    }

    public function setTransaction($transaction) {
      if ($transaction) {
        foreach ($transaction as $key => $value) {
          if (array_key_exists($key, $this->transaction)) {
            $this->transaction[$key] = $value;
          }
        }
      }
      return $this;
    }

    public function add($transaction = NULL) {
      $this->setTransaction($transaction);
      if (!$this->transaction['user_email']) {
        return NULL;
      }
      $this->db->insert('transactions', $this->transaction);
      return $this->db->lastInsertId();
    }

    public function removeAll($user = NULL) {
      if ($user) {
        $this->transaction['user_email'] = $user;
      }
      $this->db->executeUpdate("
        DELETE FROM
          transactions
        WHERE
          user_email = ?
      ", [$this->transaction['user_email']]);
      return $this;
    }

    public function getAll() {
      return $this->db->fetchAll("
        SELECT
          transactions.id,
          users.name AS user,
          categories.category,
          types.type,
          transactions.date,
          transactions.value
        FROM
          transactions
          INNER JOIN users
            ON (transactions.user_email = users.email)
          INNER JOIN categories
            ON (transactions.category_id = categories.id)
          INNER JOIN types
            ON (categories.type_id = types.id)
      ");
    }

  }
