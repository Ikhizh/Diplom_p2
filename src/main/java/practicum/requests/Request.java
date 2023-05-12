package practicum.requests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import practicum.models.Ingredients;
import practicum.models.TokenModel;
import practicum.user.UserCreds;

import static io.restassured.RestAssured.given;
import static practicum.constants.UrlConstants.*;

public class Request {
    public Request() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Extract accessToken")
    public static TokenModel requestUserToken(UserCreds userCreds) {
        return given()
                .header("Content-type", "application/json")
                .body(userCreds)
                .post(LOGIN_PATH)
                .body()
                .as(TokenModel.class);
    }

    @Step("Get ingredients")
    public static Ingredients requestIngredient() {
        return given()
                .header("Content-type", "application/json")
                .get(INGREDIENTS_URL)
                .body()
                .as(Ingredients.class);
    }
}
