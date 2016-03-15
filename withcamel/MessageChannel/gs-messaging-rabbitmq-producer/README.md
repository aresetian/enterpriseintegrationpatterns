Spring Example
==============

This example shows how to work with the simple Camel application based on the Spring Boot.

The example generates messages using timer trigger, writes them to the standard output .

You will need to compile this example first:
  mvn install

To run the example type
  mvn spring-boot:run

You can also execute the fat WAR directly:

  java -jar target/camel-example-spring-boot.war

You will see the message printed to the console every second.

To stop the example hit ctrl + c

