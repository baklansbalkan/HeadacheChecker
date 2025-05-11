# Headache Checker
***

## Application for headache checking

This server-side application is created using:
* Spring
* Hibernate
* Apache Maven
* PostgreSQL
* JPA
* REST
* JUnit
* Docker

Work is still in progress
***

## How to use

You are welcome to try it by running compose.yml\
Postgres table will be created automatically. 

Just run it and send to <localhost:8080/headache> JSON like this:
```
{
    "date": "2025-01-01",
    "isHeadache": true,
    "isMedicine": true,
    "medicine": "Medicine example",
    "intensity": 3,
    "localisation": "RIGHT",
    "timesOfDay": "EVENING",
    "comment": "Comment"
}
```

You can get all the entries here: <localhost:8080/headache>\
Or get one entry by date like this: <localhost:8080/headache/2025-01-01>\
You can edit the entry using method PUT and URL <localhost:8080/headache/2025-01-01>\
And also you can remove the entry using DELETE method on the same URL: <localhost:8080/headache/2025-01-01>

### Showing statistics

#### Getting month statistics
How many times have you headache this month? Use this URL: <localhost:8080/statistics/2025-01-01/month>\
Show all the entries: <localhost:8080/statistics/2025-01-01/month/all>\

#### Getting year statistics:
How many times have you headache this year? <localhost:8080/statistics/2025-01-01/year>\
And all the entries: <localhost:8080/statistics/2025-01-01/year/all>

