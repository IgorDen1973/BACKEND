package ru.IgorDen1973.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.IgorDen1973.dto.PostImageResponse;

import static io.restassured.RestAssured.given;
import static ru.IgorDen1973.Endpoints.DELETE_ID;
import static ru.IgorDen1973.Endpoints.UPLOAD_;

public class PostRequestTests extends BaseTest {
    private String PIC_ID;

    @Test
    void loadPictureAllFieldsExceptAlbumTest()  {
        positiveAsserts200();
        PIC_ID = given()
                .header("Authorization", token)
                .params("type","Response.file")
                .params("name","Response.name")
                .params("title","Response.title")
                .params("description","Response.description")
                .multiPart(responceCheckMultiPartSpec)
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
    void loadPictureJustFieldAlbumTest() {
        negativeAsserts417();
        PIC_ID = given()
                .header("Authorization", token)
                .params("album","Response.album")
                .multiPart(responceCheckMultiPartSpec)
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
    void loadPictureNoFileAttachedTest() {
        negativeAsserts400();
        PIC_ID = given()
                .header("Authorization", token)
                .multiPart("image","null")
                .when()
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
    void loadPicturePdfFormatTest() {
        negativeAsserts417();
        PIC_ID = given()
                .header("Authorization", token)
                .multiPart(pdfFileMultiPartSpec)
                .when()
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
    void loadPictureNotImageFormatTest() {
        negativeAsserts400();
        PIC_ID = given()
                .header("Authorization", token)
                .multiPart(notPixFileMultiPartSpec)
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
    void loadPictureBase64FormatTest() {
        positiveAsserts200();
        PIC_ID = given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .multiPart(base64MultiPartSpec)
                .params("type","base64")
                .post(UPLOAD_)
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData()
                .getId();
    }

    @Test
    void loadPictureUrlFormatTest() {

        positiveAsserts200();
        PIC_ID = given()
                .header("Authorization", token)
                .multiPart("image", "https://proprikol.ru/wp-content/uploads/2020/04/kartinki-vikingi-1.jpg")
                .params("type","url")
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
    void loadPictureBinaryFormatTest() {
        negativeAsserts400();
        PIC_ID = given()
                .header("Authorization", token)
                .multiPart(binaryFileMultiPartSpec)
                .post(UPLOAD_)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData()
                .getId();
    }

   @AfterEach
    void cleaningServiceTest() {

        if (PIC_ID == null) {
            System.out.println("************** NO NEED TO CLEAN ANYTHING THIS CASE....  *****************");
        }
        else {
            positiveAsserts200();
        given()
                .headers("Authorization", token)
                .when()
                .delete(DELETE_ID, PIC_ID)
                .prettyPeek();}
    }

}





