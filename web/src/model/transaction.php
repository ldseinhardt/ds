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

    public function setTransaction($transaction) {
      if ($transaction) {
        foreach ($transaction as $key => $value) {
          if (array_key_exists($key, $this->user)) {
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
      $this->transaction['id'] = $this->db->lastInsertId();
      return $this->transaction['id'];
    }

    private function update($transaction = NULL) {
      $this->setTransaction($transaction);

      $fields = array_filter($this->transaction, function($e) {
    		return $e !== NULL;
    	});
    	unset($fields['id']);

    	$values = array_values($fields);
    	$values[] = $this->transaction['id'];

    	$fields = implode(', ', array_map(function ($e) {
    		return $e . ' = ?';
    	}, array_keys($fields)));

      $this->db->executeUpdate("
        UPDATE
          transactions
        SET {$fields}
        WHERE
          id = ?
      ", $values);

      return $this;
    }

  }
