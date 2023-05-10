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

public class UserCreationPositiveTest {
    private User user;
    private UserRequest userRequest;
    private UserCreds userCreds;

    @Before
    public void setUp() {
        user = new User(UserDataCreation.email, UserDataCreation.password, UserDataCreation.name);
        userRequest = new UserRequest();
        userCreds = new UserCreds(UserDataCreation.email, UserDataCreation.password);
    }

    @After
    public void tearDown() {
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        userRequest
                .delete(user, tokenResponse);
    }

    @DisplayName("POST /auth/register with all fields successfully")
    @Test
    public void createUserWithValidData() {
        userRequest
                .create(user)
                .statusCode(200)
                .assertThat().body("success", is(true));
    }

}
