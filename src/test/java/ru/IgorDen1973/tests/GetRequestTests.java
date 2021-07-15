package ru.IgorDen1973.tests;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.IgorDen1973.Path2pic;
import ru.IgorDen1973.dto.PostImageResponse;

import java.io.File;

import static io.restassured.RestAssured.given;
import static ru.IgorDen1973.Endpoints.*;


public class GetRequestTests extends BaseTest{

    private String PIC_ID;
    static RequestSpecification requestSpecificationWithTokenAndMultipart;

    @BeforeEach
    void loadPictureAndGetDataTest() {

        requestSpecificationWithTokenAndMultipart = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addParam("title","Picture")
                .addMultiPart(pic4getMultiPartSpec)
                .build();

        PIC_ID = given(requestSpecificationWithTokenAndMultipart)
                .post(UPLOAD_)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData()
                .getId();
    }

    @Test
    void getExistingPicWithTokenTest() {
        positiveAsserts200();
        given(requestSpecificationWithTokenAndMultipart)
                .get(UPLOAD_ID, PIC_ID)
                .prettyPeek();
    }

    @Test
    void getExistingPicWithClientIdTest() {
        positiveAsserts200();
        given()
                .header("Authorization", clientId)
                .when()
                .get(UPLOAD_ID, PIC_ID)
                .prettyPeek();
    }

    @Test
    void getExistingPicUnauthorizedTest() {
        negativeAsserts401();
        given()
                .get(UPLOAD_ID, PIC_ID)
                .prettyPeek();
    }

    @AfterEach
    void cleaningServiceTest() {
        given()
                .headers("Authorization", token)
                .when()
                .delete(DELETE_ID, PIC_ID)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
