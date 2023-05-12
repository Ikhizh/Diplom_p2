package practicum.requests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import practicum.models.OrderData;
import practicum.models.TokenModel;
import practicum.util.Utils;

import static io.restassured.RestAssured.given;
import static practicum.constants.UrlConstants.*;

public class OrderRequest {

    public OrderRequest() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Create order without login")
    public ValidatableResponse createOrder(OrderData orderData) {
        return given()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Create order with login")
    public ValidatableResponse createOrder(OrderData orderData, TokenModel tokenResponse) {
        return given()
                .auth().oauth2(Utils.getTokenWithoutBearer(tokenResponse.getAccessToken()))
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Get orders for user with login")
    public ValidatableResponse getOrdersForUser(TokenModel tokenResponse) {
        return given()
                .auth().oauth2(Utils.getTokenWithoutBearer(tokenResponse.getAccessToken()))
                .header("Content-type", "application/json")
                .when()
                .get(ORDERS)
                .then();
    }

    @Step("Get all orders without login")
    public ValidatableResponse getOrdersAll() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(ALL_ORDERS)
                .then();
    }

}
