CREATE DATABASE IF NOT EXISTS transport_db;
USE transport_db;
CREATE TABLE IF NOT EXISTS bus_pass(
  pass_id VARCHAR(30) PRIMARY KEY,
  reg_no VARCHAR(30),
  name VARCHAR(80),
  route_no INT,
  amount DOUBLE
);
CREATE TABLE IF NOT EXISTS users_test(
  id VARCHAR(50) PRIMARY KEY,
  pwd VARCHAR(100)
);
