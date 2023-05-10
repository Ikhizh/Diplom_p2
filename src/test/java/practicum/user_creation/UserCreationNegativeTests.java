package practicum.user_creation;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.StringUtils;
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

public class UserCreationNegativeTests {
    private User user;
    private UserRequest userRequest;
    private UserCreds userCreds;
    private static String email;
    private static String password;
    private static String name;

    @Before
    public void setUp() {
        userRequest = new UserRequest();
    }

    @After
    public void coolDown() {
        if (StringUtils.equalsAny(null, email, password, name)) {
            return;
        }

        userCreds = new UserCreds(email, password);
        TokenModel tokenResponse = Request.requestUserToken(userCreds);
        userRequest
                .delete(user, tokenResponse);
    }

    @DisplayName("POST /auth/register without the email")
    @Test
    public void createUserWithoutEmail() {
        email = null;
        password = UserDataCreation.password;
        name = UserDataCreation.name;
        user = new User(email, password, name);
        userRequest
                .create(user)
                .statusCode(403)
                .assertThat().body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @DisplayName("POST /auth/register without the password")
    @Test
    public void createUserWithoutPassword() {
        email = UserDataCreation.email;
        password = null;
        name = UserDataCreation.name;
        user = new User(email, password, name);
        userRequest
                .create(user)
                .statusCode(403)
                .assertThat().body("success", is(false))
                .body("message", is("Email, password and name are required fields"));

    }

    @DisplayName("POST /auth/register without the name")
    @Test
    public void createUserWithoutName() {
        email = UserDataCreation.email;
        password = UserDataCreation.password;
        name = null;
        user = new User(email, password, name);
        userRequest
                .create(user)
                .statusCode(403)
                .assertThat().body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

}