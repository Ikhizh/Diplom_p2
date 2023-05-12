package practicum.orderslist;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.models.TokenModel;
import practicum.requests.OrderRequest;
import practicum.requests.Request;
import practicum.requests.UserRequest;
import practicum.testdata.UserDataCreation;
import practicum.user.User;
import practicum.user.UserCreds;

import static org.hamcrest.Matchers.*;

public class GetAllOrdersTests {
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

    @DisplayName("GET / all orders for all users - check quantity of orders")
    @Test
    public void getAllOrdersAll() {
        orderRequest
                .getOrdersAll()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .body("total", greaterThan(0))
                .body("totalToday", greaterThan(0))
                .body("$", hasKey("orders"));
    }

    @DisplayName("GET /orders for the user - check for the quantity of orders")
    @Test
    public void getAlUserOrders() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        orderRequest
                .getOrdersForUser(tokenResponse)
                .statusCode(200)
                .assertThat().body("success", is(true))
                .body("total", greaterThan(0))
                .body("totalToday", greaterThan(0));
    }
}
