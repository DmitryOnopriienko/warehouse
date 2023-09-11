DROP TABLE IF EXISTS waybill_has_products;
DROP TABLE IF EXISTS waybill;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS customer;

DROP SCHEMA IF EXISTS warehouse;

CREATE SCHEMA IF NOT EXISTS warehouse;

USE warehouse;

CREATE TABLE IF NOT EXISTS customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(20) NOT NULL,
    surname VARCHAR(20) NOT NULL,
    patronymic VARCHAR(20),
    email VARCHAR(40) NOT NULL,
    phone_number VARCHAR(15),
    birthday DATE,
    comment VARCHAR(1024)
);

CREATE TABLE IF NOT EXISTS product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    price DECIMAL(9,2) NOT NULL,
    about VARCHAR(1024),
    amount INT NOT NULL
);

CREATE TABLE IF NOT EXISTS waybill (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    `date` DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE IF NOT EXISTS waybill_has_products (
    waybill_id INT NOT NULL,
    product_id INT NOT NULL,
    amount INT NOT NULL,
    FOREIGN KEY (waybill_id) REFERENCES waybill(id),
    FOREIGN KEY (product_id) REFERENCES customer(id),
    PRIMARY KEY (waybill_id, product_id)
);

INSERT INTO customer(`first_name`, surname, email)
VALUES ('Ihor', 'Ihorev', 'ihorevihor@ihoho.hor'), ('Vitaliy', 'Vitaliev', 'vitaliy.v@vfor.vendetta');

INSERT INTO product(title, price, amount)
VALUES ('Foo', 99.99, 100), ('Bar', 999.99, 5);

INSERT INTO waybill(customer_id, `date`)
VALUES (1, '2023-09-10'), (2, '2023-09-01');

INSERT INTO waybill_has_products(waybill_id, product_id, amount)
VALUES (1, 1, 5), (1, 2, 1),
       (2, 1, 10), (2, 2, 2);
