CREATE TABLE IF NOT EXISTS TODO (
  id INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  contents VARCHAR(100),
  createdDate VARCHAR(20),
  modifiedDate VARCHAR(20),
  complete BOOLEAN,
  refId VARCHAR(100)
);