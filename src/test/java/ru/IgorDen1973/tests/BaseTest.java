package ru.IgorDen1973.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import ru.IgorDen1973.Path2pic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

import static org.hamcrest.Matchers.equalTo;
import static ru.IgorDen1973.Path2pic.*;

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
    static MultiPartSpecification responceCheckMultiPartSpec;
    static MultiPartSpecification pdfFileMultiPartSpec;
    static MultiPartSpecification notPixFileMultiPartSpec;
    static MultiPartSpecification binaryFileMultiPartSpec;
    static MultiPartSpecification base64MultiPartSpec ;
    static MultiPartSpecification pic4getMultiPartSpec;
    static String encodedFile;

    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        getProperties();
        RestAssured.baseURI = "https://api.imgur.com/3";
        token = properties.getProperty("token");
        username = properties.getProperty("username");
        clientId = properties.getProperty("clientId");

        byte[] byteArray = getFileContent(REGULAR.getSticker());
        encodedFile = Base64.getEncoder().encodeToString(byteArray);


        requestSpecificationWithToken = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();

        responceCheckMultiPartSpec = new MultiPartSpecBuilder(new File(RESPONCE_CHECK.getSticker()))
                .controlName("image")
                .build();
        pdfFileMultiPartSpec = new MultiPartSpecBuilder(new File(PDF.getSticker()))
                .controlName("image")
                .build();
        notPixFileMultiPartSpec = new MultiPartSpecBuilder(new File(NOT_IMAGE.getSticker()))
                .controlName("image")
                .build();
        binaryFileMultiPartSpec = new MultiPartSpecBuilder(new File(BINARY.getSticker()))
                .controlName("image")
                .build();
        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();
        pic4getMultiPartSpec = new MultiPartSpecBuilder(new File(Path2pic.REGULAR.getSticker()))
                .controlName("image")
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

    private static byte[] getFileContent(String path) {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }


}