# Headache Checker

## Application for headache checking

This server-side application is created using:
* Spring
* JPA
* REST
* Hibernate
* Apache Maven
* PostgreSQL + Liquibase
* JUnit + AssertJ
* Docker
* Spring Security JWT
* Swagger

Work is still in progress
***

## How to use

You are welcome to try it by running compose.yml\
Postgres table will be created automatically by Liquibase

Just run the app and go to <http://localhost:8080/swagger-ui/index.html>

Firstly, you need to create a new user. Feel free to use auth-controller in order to sign up and sign in\
Then you can create headache entries using headache-controller and check your statistics using statistics-controller\
Also, you can add a plethora of users and manage them using admin role and admin-controller
