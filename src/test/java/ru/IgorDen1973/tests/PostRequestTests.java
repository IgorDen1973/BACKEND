package ru.IgorDen1973.tests;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.IgorDen1973.dto.PostImageResponse;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static ru.IgorDen1973.Endpoints.DELETE_ID;
import static ru.IgorDen1973.Endpoints.UPLOAD_;
import static ru.IgorDen1973.Path2pic.*;

public class PostRequestTests extends BaseTest {

    private String PIC_ID;
    static String encodedFile;
    MultiPartSpecification responceCheckMultiPartSpec;
    MultiPartSpecification pdfFileMultiPartSpec;
    MultiPartSpecification notPixFileMultiPartSpec;
    MultiPartSpecification binaryFileMultiPartSpec;
    MultiPartSpecification base64MultiPartSpec;


    @Test
    void loadPictureAllFieldsExceptAlbumTest()  {
        responceCheckMultiPartSpec = new MultiPartSpecBuilder(new File(RESPONCE_CHECK.getSticker()))
                .controlName("image")
                .build();
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
        responceCheckMultiPartSpec = new MultiPartSpecBuilder(new File(RESPONCE_CHECK.getSticker()))
                .controlName("image")
                .build();
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
        pdfFileMultiPartSpec = new MultiPartSpecBuilder(new File(PDF.getSticker()))
                .controlName("image")
                .build();
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
        notPixFileMultiPartSpec = new MultiPartSpecBuilder(new File(NOT_IMAGE.getSticker()))
                .controlName("image")
                .build();
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
        byte[] byteArray = getFileContent(REGULAR.getSticker());
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

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
        binaryFileMultiPartSpec = new MultiPartSpecBuilder(new File(BINARY.getSticker()))
                .controlName("image")
                .build();
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

    private byte[] getFileContent(String path) {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

}





