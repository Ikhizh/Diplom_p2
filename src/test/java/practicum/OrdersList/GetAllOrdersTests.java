package practicum.OrdersList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.Models.TokenModel;
import practicum.requests.Request;
import practicum.requests.UserRequest;
import practicum.testData.UserDataCreation;
import practicum.user.User;
import practicum.user.UserCreds;
import practicum.util.Utils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetAllOrdersTests {
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
    public void getAllOrders() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("https://stellarburgers.nomoreparties.site/api/orders/all")
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .body("total", greaterThan(0))
                .body("totalToday", lessThan(51))
                .body("$", hasKey("orders"));
    }

    @Test
    public void getAlUserOrders() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        given()
                .auth().oauth2(Utils.getTokenWithoutBearer(tokenResponse.getAccessToken()))
                .header("Content-type", "application/json")
                .when()
                .get("https://stellarburgers.nomoreparties.site/api/orders")
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .body("total", greaterThan(0))
                .body("totalToday", lessThan(51));
    }
}
