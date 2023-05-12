package practicum.requests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import practicum.models.TokenModel;
import practicum.user.User;
import practicum.user.UserCreds;
import practicum.util.Utils;

import static io.restassured.RestAssured.given;
import static practicum.constants.UrlConstants.*;

public class UserRequest {
    public UserRequest() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Register user")
    public ValidatableResponse create(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(REGISTER_PATH)
                .then();
    }

    @Step("Login user")
    public ValidatableResponse login(UserCreds creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Delete user")
    public void delete(User user, TokenModel tokenResponse) {
        given()
                .auth().oauth2(Utils.getTokenWithoutBearer(tokenResponse.getAccessToken()))
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .delete(USER_PATH)
                .then()
                .statusCode(202);
    }
}