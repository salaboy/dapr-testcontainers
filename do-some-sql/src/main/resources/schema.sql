CREATE TABLE IF NOT EXISTS customers (id SERIAL PRIMARY KEY,
                                  name varchar(50) NOT NULL,
                                  lastname varchar(50) NOT NULL
);
CREATE SEQUENCE customer_seq MINVALUE 1 START WITH 1 INCREMENT BY 1;