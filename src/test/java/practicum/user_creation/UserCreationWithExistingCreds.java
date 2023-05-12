package practicum.user_creation;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.models.TokenModel;
import practicum.requests.Request;
import practicum.requests.UserRequest;
import practicum.testdata.UserDataCreation;
import practicum.user.User;
import practicum.user.UserCreds;
import static org.hamcrest.Matchers.is;


public class UserCreationWithExistingCreds {

    private User user;
    private UserRequest userRequest;
    private UserCreds userCreds;

    @Before
    public void setUp() {
        user = new User(UserDataCreation.email, UserDataCreation.password, UserDataCreation.name);
        userRequest = new UserRequest();
        userCreds = new UserCreds(UserDataCreation.email, UserDataCreation.password);
        userRequest
                .create(user)
                .statusCode(200)
                .assertThat().body("success", is(true));
    }

    @After
    public void tearDown() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        userRequest
                .delete(user, tokenResponse);
    }

    @DisplayName("POST /auth/register with the same credentials")
    @Test
    public void createUserWithExistingCreds() {
        userRequest
                .create(user)
                .statusCode(403)
                .assertThat().body("success", is(false))
                .body("message", is("User already exists"));
    }
}
