package practicum.orderCreationTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.Models.Ingredients;
import practicum.Models.TokenModel;
import practicum.Models.OrderData;
import practicum.requests.Request;
import practicum.requests.UserRequest;
import practicum.testData.UserDataCreation;
import practicum.user.User;
import practicum.user.UserCreds;
import practicum.util.Utils;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class OrderCreationTest {

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

    @Test
    public void createOrder() {
        Ingredients ingredients = Request.requestIngredient();
        List<String> ingredientsId = Utils.getIngredientsId(ingredients);
        List<String> randomElements = Utils.getRandomElements(2, ingredientsId);
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        OrderData orderData = new OrderData(randomElements);
        given()
                .auth().oauth2(Utils.getTokenWithoutBearer(tokenResponse.getAccessToken()))
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/orders")
                .then().statusCode(200)
                .assertThat()
                .body("success", is(true))
                .body("order.number", is(greaterThan(0)));
    }

    @Test
    public void createOrderWithoutAuthorization() {
        Ingredients ingredients = Request.requestIngredient();
        List<String> ingredientsId = Utils.getIngredientsId(ingredients);
        List<String> randomElements = Utils.getRandomElements(2, ingredientsId);
        OrderData orderData = new OrderData(randomElements);
        given()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/orders")
                .then().statusCode(200)
                .assertThat()
                .body("success", is(true))
                .body("order.number", is(greaterThan(0)));
    }

    @Test
    public void createOrderWithoutIngredients() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        String orderData = "";
        given()
                .auth().oauth2(Utils.getTokenWithoutBearer(tokenResponse.getAccessToken()))
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/orders")
                .then().statusCode(400)
                .assertThat()
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    public void createUserWithIncorrectIngredientsHash() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        OrderData orderData = new OrderData(Arrays.asList("60d3b41111abdacab0026a733c6"));
        given()
                .auth().oauth2(Utils.getTokenWithoutBearer(tokenResponse.getAccessToken()))
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/orders")
                .then().statusCode(500);
    }

}
