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

    public function getCategories() {
      $query = $this->db->fetchAll("
        SELECT
          categories.id,
          concat(categories.category, ' (', types.type, ')') AS name
        FROM
          categories
          LEFT JOIN types
            ON (categories.type_id = types.id)
        ORDER BY(categories.id)
      ");
      return $query;
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

    public function clearTransactions() {
      $this->db->executeUpdate("
        TRUNCATE TABLE transactions
      ");
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
          LEFT JOIN users
            ON (transactions.user_email = users.email)
          LEFT JOIN categories
            ON (transactions.category_id = categories.id)
          LEFT JOIN types
            ON (categories.type_id = types.id)
        ORDER BY users.name, transactions.id
      ");
    }

    public function getReportfilters($filters) {
      if ($filters) {
        $filters = array_filter($filters, function($e) {
          return $e;
        });
        if (count($filters)) {
          $conditions = [];
          foreach ($filters as $key => $value) {
            switch ($key) {
              case 'date_initial':
                $conditions[] = "transactions.date >= '{$value}'";
                break;
              case 'date_final':
                $conditions[] = "transactions.date <= '{$value}'";
                break;
              case 'category_id':
                $conditions[] = "transactions.category_id = '{$value}'";
                break;
              case 'type_id':
                $conditions[] = "categories.type_id = '{$value}'";
                break;
              case 'birthyear':
                $conditions[] = "users.birthyear = '{$value}'";
                break;
              case 'education_id':
                $conditions[] = "users.education_id = '{$value}'";
                break;
              case 'location':
                $conditions[] = "concat_ws(', ', cities.city, states.state, countries.country) = '{$value}'";
                break;
              case 'occupation':
                $conditions[] = "occupations.occupation = '{$value}'";
                break;
            }
          }
          $where = implode(' AND ', $conditions);
          return "
            WHERE
              {$where}
          ";
        }
      }
      return '';
    }

    public function getReportExpenseVsIncome($filters = NULL) {
      $query = $this->db->fetchAll("
        SELECT
          types.type,
          SUM(transactions.value) AS total
        FROM
          transactions
          LEFT JOIN categories
            ON (transactions.category_id = categories.id)
          LEFT JOIN types
            ON (categories.type_id = types.id)
          LEFT JOIN users
            ON (transactions.user_email = users.email)
          LEFT JOIN cities
            ON (users.city_id = cities.id)
          LEFT JOIN states
            ON (cities.state_id = states.id)
          LEFT JOIN countries
            ON (states.country_id = countries.id)
          LEFT JOIN occupations
            ON (users.occupation_id = occupations.id)
        {$this->getReportfilters($filters)}
        GROUP BY (categories.type_id)
        ORDER BY (categories.type_id) ASC
      ");
      if ($query && count($query) === 2) {
        $values = [];
        foreach ($query as $row) {
          $values[] = [
            $row['type'] . 's',
            (double) $row['total']
          ];
        }
        return $values;
      }
      return NULL;
    }

    public function getReportTransactionsPerDay($filters = NULL) {
      $query = $this->db->fetchAll("
        SELECT
          transactions.date,
          categories.type_id,
          SUM(transactions.value) AS total
        FROM
          transactions
          LEFT JOIN categories
            ON (transactions.category_id = categories.id)
          LEFT JOIN types
            ON (categories.type_id = types.id)
          LEFT JOIN users
            ON (transactions.user_email = users.email)
          LEFT JOIN cities
            ON (users.city_id = cities.id)
          LEFT JOIN states
            ON (cities.state_id = states.id)
          LEFT JOIN countries
            ON (states.country_id = countries.id)
          LEFT JOIN occupations
            ON (users.occupation_id = occupations.id)
        {$this->getReportfilters($filters)}
        GROUP BY transactions.date, categories.type_id
        ORDER BY transactions.date, categories.type_id
      ");
      if ($query && count($query)) {
        $temp = [];
        foreach ($query as $row) {
          if (!(isset($temp[$row['date']]) && count($temp[$row['date']]))){
            $temp[$row['date']] = [0, 0];
          }
          $temp[$row['date']][$row['type_id'] -1] = (double) $row['total'];
        }
        $values = [];
        foreach ($temp as $key => $value) {
          $values[] = [$key, $value[0], $value[1]];
        }
        return $values;
      }
      return NULL;
    }

    public function getReportExpensesByCategory($filters = NULL) {
      $query = $this->db->fetchAll("
        SELECT
          categories.category,
          SUM(transactions.value) AS total
        FROM
          transactions
          JOIN categories
            ON (transactions.category_id = categories.id
              AND categories.type_id = 1)
          LEFT JOIN users
            ON (transactions.user_email = users.email)
          LEFT JOIN cities
            ON (users.city_id = cities.id)
          LEFT JOIN states
            ON (cities.state_id = states.id)
          LEFT JOIN countries
            ON (states.country_id = countries.id)
          LEFT JOIN occupations
            ON (users.occupation_id = occupations.id)
        {$this->getReportfilters($filters)}
        GROUP BY (categories.category)
        ORDER BY (categories.category) ASC
      ");
      if ($query && count($query) > 0) {
        $values = [];
        foreach ($query as $row) {
          $values[] = [
            $row['category'],
            (double) $row['total']
          ];
        }
        return $values;
      }
      return NULL;
    }

    public function getReportIncomesByCategory($filters = NULL) {
      $query = $this->db->fetchAll("
        SELECT
          categories.category,
          SUM(transactions.value) AS total
        FROM
          transactions
          JOIN categories
            ON (transactions.category_id = categories.id
              AND categories.type_id = 2)
          LEFT JOIN users
            ON (transactions.user_email = users.email)
          LEFT JOIN cities
            ON (users.city_id = cities.id)
          LEFT JOIN states
            ON (cities.state_id = states.id)
          LEFT JOIN countries
            ON (states.country_id = countries.id)
          LEFT JOIN occupations
            ON (users.occupation_id = occupations.id)
        {$this->getReportfilters($filters)}
        GROUP BY (categories.category)
        ORDER BY (categories.category) ASC
      ");
      if ($query && count($query) > 0) {
        $values = [];
        foreach ($query as $row) {
          $values[] = [
            $row['category'],
            (double) $row['total']
          ];
        }
        return $values;
      }
      return NULL;
    }

    // outros relatórios ...

  }
