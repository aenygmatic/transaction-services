# Getting Started

## Setup dev environment

### Couchbase

* Install [Couchbase](https://docs.couchbase.com/server/current/install/install-intro.html)
* Create buckets `transactions` and `analytics` with the same user and password
* Apply the scripts from each services `/couchbase/indexes`

### Kafka

* Install [Kafka](https://kafka.apache.org/quickstart)
* The application will try to connect to `localhost:9092` by default
* Create a topic named `transaction-updates`
* Create topic`./kafka-topics.sh --create --topic transaction-updates --bootstrap-server localhost:9092`
* Console consumer for
  debug `./kafka-console-consumer.sh --topic transaction-updates --from-beginning --bootstrap-server localhost:29092`
* Delete topic for cleanup `./kafka-topics.sh --zookeeper localhost:2181 --delete --topic transaction-updates`

### Build

* Install [IntelliJ](https://www.jetbrains.com/idea/download/?section=windows)
* Import this project as a Gradle project
* Build: Run `gradle build`
* Test: Run `gradle test`
* Integration Test: Run `gradle integrationtest` (for this you need both Couchbase and Kafka running)

## Services

### transaction-api

The transaction-api service is responsible for CRUD operations on transactions and also fires an event to Kafka about
the changes.

#### Out-of-scope

* Proper validation on all transaction data
* Proper error model for validation
* Paging and sorting
* Event replay

The server starts at port `8080`

### transaction-analytics

The transaction-analytics service is responsible for creating monthly user level analysis based on the transaction
event.

#### The analytics contains:

* Projection for spending and income based on the last month
* Up-to-date total and category-based statistics
* Each value is in multi-currency

#### Out-of-scope

* Re-generation of analytics
* Currency conversion for display currency

The server starts at port `8081`