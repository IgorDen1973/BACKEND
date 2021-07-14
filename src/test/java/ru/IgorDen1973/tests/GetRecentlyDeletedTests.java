package ru.IgorDen1973.tests;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.IgorDen1973.dto.PostImageResponse;

import java.io.File;

import static io.restassured.RestAssured.given;
import static ru.IgorDen1973.Endpoints.DELETE_ID;
import static ru.IgorDen1973.Endpoints.UPLOAD_;
import static ru.IgorDen1973.Path2pic.REGULAR;


public class GetRecentlyDeletedTests extends BaseTest {

    private String PIC_ID;
    MultiPartSpecification responceCheckMultiPartSpec;

    @BeforeEach
    void loadPictureAndGetDataTest() {
        responceCheckMultiPartSpec = new MultiPartSpecBuilder(new File(REGULAR.getSticker()))
                .controlName("image")
                .build();
        positiveAsserts200();

        PIC_ID = given(requestSpecificationWithToken)
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
    void deleteAndTryingToGetMentionedPicTest() {
        positiveAsserts200();
        given(requestSpecificationWithToken)
                .delete(DELETE_ID, PIC_ID);

    }

    @AfterEach
    void tryingToGetDeletedPicTest() {
        negativeAsserts404();
        given(requestSpecificationWithToken)
                .get(DELETE_ID, PIC_ID)
                .prettyPeek();
    }
}
