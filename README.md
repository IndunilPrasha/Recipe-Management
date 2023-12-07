# Recipe-Management
Recipe Management API
This is a REST API application that allows users to manage their favorite recipes.

Features:
-Add a new recipe
-Update an existing recipe
-Delete a recipe
-Get all recipes
-Search recipes with filtering capabilities

Filters:
-Recipes can be searched and filtered based on:
-Vegetarian or non-vegetarian
-Number of servings
-Ingredients to include or exclude
-Text search in instructions

Some examples:

-Get all vegetarian recipes
-Recipes for 4 people having potatoes as ingredient
-Recipes without salmon having "oven" in instructions

Documentation
API Documentation can be found at:http://localhost:8080/swagger-ui/index.html

Build and Run
-Build : mvn clean install
-Run : mvn spring-boot:run

The application can be accessed at http://localhost:8080 once started.

Configure Database and server ports in application.properties file.

Technologies Used:
-Java 17
-Spring Boot 3.2.0
-H2 Database
-Maven

Author
-Indunil Prasanna
