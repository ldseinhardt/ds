# Sistema web

## Programas necessários

* PHP
* MySQL
* Bower (Gerenciador de pacotes para Front-end)
* Composer (Gerenciador de pacotes para PHP)

## Instalação de programas necessários (Ubuntu Linux)

```
$ sudo apt-get -y update
$ sudo apt-get -y install mysql-server php5 php5-mysql npm nodejs
$ sudo npm install -g bower
$ php -r "copy('https://getcomposer.org/installer', 'composer-setup.php');"
$ sudo php composer-setup.php --install-dir=/bin --filename=composer
```

## Instalação de dependências

```
$ ./install
```

## Configurar banco de dados

Configure no arquivo `src/settings.php`.

<span style="color: #f00">Favor não submeter ao git suas configurações.</span>

```php
<?php
  $debug = true;

  $db_options = [
    'driver'   => 'pdo_mysql',
    'charset'  => 'utf8',
    'host'     => 'localhost',
    'dbname'   => 'oraculo',
    'username' => 'root', //usuário
    'password' => '' // senha
  ];

  ...
```

## Instanciar a base de dados

```
$ mysql -u [usuario] -p

mysql> source ../database/create.sql
mysql> source ../database/insert.sql
mysql> source ../database/test.sql -- OPCIONAL
```

## Executar com servidor embutido no php >= 5.4

```
$ ./run
```

Verifique a versão do php
```
php -v
```

## Bibliotecas Front-end de terceiros

* [jQuery](https://github.com/jquery/jquery)
* [jQuery-Autocomplete](https://github.com/devbridge/jQuery-Autocomplete)
* [Bootstrap](https://github.com/twbs/bootstrap)
* [Bootstrap-Table](https://github.com/wenzhixin/bootstrap-table)
* [Bootstrap-Material-Design](https://github.com/FezVrasta/bootstrap-material-design)
* [Font-Awesome](http://fortawesome.github.io/Font-Awesome/)

## Framework PHP

* [Silex](http://silex.sensiolabs.org)
