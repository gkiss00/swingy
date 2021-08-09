# Instal the DB

export PATH=${PATH}:/usr/local/mysql/bin/

mysql -u root -p

alter user 'root'@'localhost' identified with mysql_native_password by 'root';
CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci; =>
CHARSET=utf8 COLLATE=utf8_general_ci;
CREATE DATABASE swingy
source swingy.sql

CREATE USER 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;


$cfg['Servers'][$i]['controluser'] = 'root';
$cfg['Servers'][$i]['controlpass'] = 'root';

//$cfg['Servers'][$i]['auth_type'] = 'config';
$cfg['Servers'][$i]['user'] = 'root';
$cfg['Servers'][$i]['password'] = 'root';