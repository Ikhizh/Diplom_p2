package practicum.requests;

import practicum.Models.Ingredients;
import practicum.Models.TokenModel;
import practicum.user.UserCreds;

import static io.restassured.RestAssured.given;

public class Request {

    public static TokenModel requestUserToken(UserCreds userCreds) {
        return given()
                .header("Content-type", "application/json")
                .body(userCreds)
                .post("api/auth/login")
                .body()
                .as(TokenModel.class);
    }

    public static Ingredients requestIngredient() {
        return given()
                .header("Content-type", "application/json")
                .get("https://stellarburgers.nomoreparties.site/api/ingredients")
                .body()
                .as(Ingredients.class);
    }
}
