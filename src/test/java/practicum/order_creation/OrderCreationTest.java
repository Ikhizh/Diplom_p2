package practicum.order_creation;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.models.Ingredients;
import practicum.models.TokenModel;
import practicum.models.OrderData;
import practicum.requests.OrderRequest;
import practicum.requests.Request;
import practicum.requests.UserRequest;
import practicum.testdata.UserDataCreation;
import practicum.user.User;
import practicum.user.UserCreds;
import practicum.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;


public class OrderCreationTest {

    private User user;
    private UserRequest userRequest;
    private UserCreds userCreds;
    private OrderRequest orderRequest;

    @Before
    public void setUp() {
        user = new User(UserDataCreation.email, UserDataCreation.password, UserDataCreation.name);
        userRequest = new UserRequest();
        userCreds = new UserCreds(UserDataCreation.email, UserDataCreation.password);
        orderRequest = new OrderRequest();
        userRequest
                .create(user);
    }

    @After
    public void tearDown() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        userRequest
                .delete(user, tokenResponse);
    }

    @DisplayName("POST /orders with valid ingredients with token")
    @Test
    public void createOrder() {
        Ingredients ingredients = Request.requestIngredient();
        List<String> ingredientsId = Utils.getIngredientsId(ingredients);
        List<String> randomElements = Utils.getRandomElements(2, ingredientsId);
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        OrderData orderData = new OrderData(randomElements);
        orderRequest
                .createOrder(orderData, tokenResponse)
                .statusCode(200)
                .assertThat()
                .body("success", is(true))
                .body("order.number", is(greaterThan(0)));
    }

    @DisplayName("POST /orders with valid ingredients without token")
    @Test
    public void createOrderWithoutAuthorization() {
        Ingredients ingredients = Request.requestIngredient();
        List<String> ingredientsId = Utils.getIngredientsId(ingredients);
        List<String> randomElements = Utils.getRandomElements(2, ingredientsId);
        OrderData orderData = new OrderData(randomElements);
        orderRequest
                .createOrder(orderData)
                .statusCode(200)
                .assertThat()
                .body("success", is(true))
                .body("order.number", is(greaterThan(0)));
    }

    @DisplayName("POST /orders without ingredients")
    @Test
    public void createOrderWithoutIngredients() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        OrderData orderData = new OrderData(new ArrayList<>());
        orderRequest
                .createOrder(orderData, tokenResponse)
                .statusCode(400)
                .assertThat()
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @DisplayName("POST /orders with invalid ingredient")
    @Test
    public void createUserWithIncorrectIngredientsHash() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        OrderData orderData = new OrderData(Arrays.asList("60d3b41111abdacab0026a733c6"));
        orderRequest
                .createOrder(orderData, tokenResponse)
                .statusCode(500);
    }

}
