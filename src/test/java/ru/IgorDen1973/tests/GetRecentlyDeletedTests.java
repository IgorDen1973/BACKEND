package ru.IgorDen1973.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.IgorDen1973.dto.PostImageResponse;

import static io.restassured.RestAssured.given;
import static ru.IgorDen1973.Endpoints.DELETE_ID;
import static ru.IgorDen1973.Endpoints.UPLOAD_;


public class GetRecentlyDeletedTests extends BaseTest {
    private String PIC_ID;


    @BeforeEach
    void loadPictureAndGetDataTest() {

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
