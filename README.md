# SF Food Truck API

An API that returns the closest San Francisco food trucks to you based off the data provided by DataSF.

## Getting Started
This application targets Java 11, so make sure that's installed.

    git clone git@github.com:gizmodude4/sf-food-truck-api.git
    cd sf-food-truck-api
    mvn clean package
    java -jar application/target/application*.jar server config.yaml

## Running on Docker locally
After running `mvn clean install`, run the following commands:

    docker build -t sf-food-truck-api application/
    docker run -p 8080:8080 sf-food-truck-api:latest

## Swagger docs
To test the API out for yourself, use one of the two methods above to start running the application, then point your browser to `localhost/swagger`.