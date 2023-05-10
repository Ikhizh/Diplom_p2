package practicum.user_update;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.models.CreatedUser;
import practicum.models.TokenModel;
import practicum.requests.Request;
import practicum.requests.UserRequest;
import practicum.testdata.UserDataCreation;
import practicum.user.User;
import practicum.user.UserCreds;
import practicum.util.Utils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static practicum.constants.UrlConstants.USER_PATH;

public class UserDataUpdateTests {
    private User user;
    private UserRequest userRequest;
    private UserCreds userCreds;

    @Before
    public void setUp() {
        user = new User(UserDataCreation.email, UserDataCreation.password, UserDataCreation.name);
        userRequest = new UserRequest();
        userCreds = new UserCreds(UserDataCreation.email, UserDataCreation.password);
        userRequest
                .create(user);
    }

    @After
    public void tearDown() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        userRequest
                .delete(user, tokenResponse);
    }

    @DisplayName("PATCH /auth/user with valid token")
    @Test
    public void userUpdate() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        CreatedUser createdUser = new CreatedUser();
        String newName = "newname3";
        String newEmail = "newemail3@new.com";
        createdUser.setName(newName);
        createdUser.setEmail(newEmail);
        userCreds.setEmail(newEmail);
        given()
                .auth().oauth2(Utils.getTokenWithoutBearer(tokenResponse.getAccessToken()))
                .header("Content-type", "application/json")
                .body(createdUser)
                .when()
                .patch(USER_PATH)
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail))
                .body("user.name", equalTo(newName));
    }

    @DisplayName("PATCH /auth/user without token")
    @Test
    public void userUpdateWithoutLogIn() {
        CreatedUser createdUser = new CreatedUser();
        String newName = "newname3";
        String newEmail = "newemail3@new.com";
        createdUser.setName(newName);
        createdUser.setEmail(newEmail);
        given()
                .header("Content-type", "application/json")
                .body(createdUser)
                .when()
                .patch(USER_PATH)
                .then()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", is("You should be authorised"));
    }

}