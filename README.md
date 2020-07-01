# How to run

### Running the Spring Boot web service
1. Install all Maven dependencies defined in pom.xml, your IDE can do this for you. 
2. Run the application either by 
    - running the main application class `SpringbootReactAppApplication` from IDE
    - or
    - executing `mvn spring-boot:run` from terminal
3. Now that the web service is up and running you can test it with [Postman](https://www.postman.com/). Set up an account and download the application if you haven't already.
4. Use Postman to make requests to the endpoints defined in `StudentController` class.
    - requests should start with `http://localhost:8080/api/students`

### Disable security
If you're testing your backend features with Postman, you should disable security features. 
1. comment out `WebSecurityConfig` class 
2. comment out OAuth2 dependency in pom.xml:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```
Then restart the web server. This way you don't have to include tokens with every request, and can focus on the application logic.

### Running the application with React client
1. Install [NodeJs](https://nodejs.org/en/)
2. Open another terminal and cd into the `src > js` directory. 
    - Then execute `npm install` to install frontend dependencies
3. run `npm start` from the js directory
    - Your Spring boot web service should be running in a separate terminal
    - browser tab should automatically open at `http://localhost:3000/`
