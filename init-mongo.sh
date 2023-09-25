#!/bin/bash
mongosh mongodb://localhost:27017/warehouse?authSource=admin -u root -p toor <<EOF
use warehouse;

db.createCollection("customer");
db.createCollection("product");
db.createCollection("waybill");

db.customer.insertMany([
                {
                  "first_name": "Ihor",
                  "surname": "Ihorev",
                  "patronymic": "Ihorevich",
                  "email": "iii@gmail.hhh",
                  "phone_number": "+38 050 192 83 74",
                  "birthday": "2003-09-09"
                },
                {
                  "first_name": "Alex",
                  "surname": "Alexov",
                  "patronymic": "Alexovich",
                  "email": "aaa@gmail.aaa",
                  "phone_number": "+38 050 192 83 74",
                  "birthday": "2003-09-09"
                },
                {
                  "first_name": "Ivan",
                  "surname": "Ivanov",
                  "patronymic": "Ivanovich",
                  "email": "vvv@gmail.vvv",
                  "phone_number": "+38 050 192 83 74",
                  "birthday": "2003-09-09"
                }
              ]);

db.product.insertMany([
                        {
                          "title": "Shawarma XXL",
                          "price": 29.99,
                          "amount": 1000,
                          "about": "the best food"
                        },
                        {
                          "title": "Pizza",
                          "price": 59.99,
                          "amount": 100
                        },
                        {
                          "title": "Burger",
                          "price": 39.99,
                          "amount": 50,
                          "about": "we love meat and cheese"
                        }
                      ]);

var product_ids = [];
var customer_ids = [];

db.product.find({}, { _id: 1 }).forEach(function(product) {
    product_ids.push(product._id);
});

db.customer.find({}, { _id: 1 }).forEach(function(customer) {
    customer_ids.push(customer._id);
});

function getRandomElementFromArray(array) {
    return array[Math.floor(Math.random() * array.length)];
}

var product1_id = getRandomElementFromArray(product_ids);
var product2_id = getRandomElementFromArray(product_ids);

var customer_id = getRandomElementFromArray(customer_ids);

db.waybill.insertMany([
                        {
                          "date": ISODate("2020-12-23"),
                          "customer_id": customer_id,
                          "products": [
                            {
                              "product_id": product1_id,
                              "amount": 2
                            },
                            {
                              "product_id": product2_id,
                              "amount": 4
                            }
                          ]
                        },
                        {
                          "date": ISODate("2021-12-23"),
                          "customer_id": customer_id,
                          "products": [
                            {
                              "product_id": product1_id,
                              "amount": 6
                            },
                            {
                              "product_id": product2_id,
                              "amount": 8
                            }
                          ]
                        }
                      ]);

EOF