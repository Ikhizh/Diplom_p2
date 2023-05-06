package practicum.LoginTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.Models.TokenModel;
import practicum.requests.Request;
import practicum.requests.UserRequest;
import practicum.testData.UserDataCreation;
import practicum.user.User;
import practicum.user.UserCreds;

import static org.hamcrest.Matchers.is;

public class LoginPositiveTest {
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
    public void tearDown(){
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        userRequest
                .delete(user, tokenResponse);
    }

    @Test
    public void loginWithValidData() {
        userRequest
                .login(UserCreds.credsFrom(user))
                .statusCode(200)
                .assertThat()
                .body("success", is(true));
    }

}