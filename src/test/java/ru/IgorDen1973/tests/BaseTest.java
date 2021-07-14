package ru.IgorDen1973.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.Matchers.equalTo;

public abstract class BaseTest {
    static Properties properties = new Properties();
    static ResponseSpecification positiveResponseSpecification200;
    static ResponseSpecification negativeResponseSpecification417;
    static ResponseSpecification negativeResponseSpecification400;
    static ResponseSpecification negativeResponseSpecification401;
    static ResponseSpecification negativeResponseSpecification404;
    static RequestSpecification requestSpecificationWithToken;
    static String token;
    static String username;
    static String clientId;


    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        getProperties();
        RestAssured.baseURI = "https://api.imgur.com/3";
        token = properties.getProperty("token");
        username = properties.getProperty("username");
        clientId = properties.getProperty("clientId");

        requestSpecificationWithToken = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();
    }

    static void positiveAsserts200() {

        positiveResponseSpecification200 = new ResponseSpecBuilder()
                .expectBody("success", equalTo(true))
                .expectBody("status", equalTo(200))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();
        RestAssured.responseSpecification = positiveResponseSpecification200;
    }


    static void negativeAsserts417() {
        negativeResponseSpecification417 = new ResponseSpecBuilder()
                .expectBody("success", equalTo(false))
                .expectBody("status", equalTo(417))
                .expectBody("data.error",equalTo("Internal expectation failed"))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(417)
                .expectHeader("Connection",equalTo("close") )
                .build();
        RestAssured.responseSpecification = negativeResponseSpecification417;
    }

    static void negativeAsserts400() {

        negativeResponseSpecification400 = new ResponseSpecBuilder()
                .expectBody("success", equalTo(false))
                .expectBody("status", equalTo(400))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(400)
                .build();
        RestAssured.responseSpecification = negativeResponseSpecification400;
    }

    static void negativeAsserts401() {

        negativeResponseSpecification400 = new ResponseSpecBuilder()
                .expectBody("success", equalTo(false))
                .expectBody("status", equalTo(401))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(401)
                .build();
        RestAssured.responseSpecification = negativeResponseSpecification401;
    }

    static void negativeAsserts404() {

        negativeResponseSpecification404 = new ResponseSpecBuilder()
                .expectStatusCode(404)
                .build();
        RestAssured.responseSpecification = negativeResponseSpecification404;
    }


    static void getProperties(){
        try (InputStream output = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(output);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}