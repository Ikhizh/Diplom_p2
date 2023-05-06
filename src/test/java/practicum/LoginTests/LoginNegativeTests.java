package practicum.LoginTests;

import org.junit.Before;
import org.junit.Test;
import practicum.requests.UserRequest;
import practicum.testData.UserDataCreation;
import practicum.user.UserCreds;

import static org.hamcrest.Matchers.is;

public class LoginNegativeTests {
    private UserRequest userRequest;
    private UserCreds incorrectUserCreds;

    @Before
    public void setUp() {
        userRequest = new UserRequest();
    }

    @Test
    public void loginWithoutPassword() {
        incorrectUserCreds = new UserCreds(UserDataCreation.email, null);
        userRequest
                .login(incorrectUserCreds).statusCode(401)
                .assertThat().body("message", is("email or password are incorrect"));
    }

    @Test
    public void loginWithoutEmail() {
        incorrectUserCreds = new UserCreds(null, UserDataCreation.password);
        userRequest
                .login(incorrectUserCreds).statusCode(401)
                .assertThat().body("message", is("email or password are incorrect"));
    }

    @Test
    public void loginWithIncorrectPassword() {
        incorrectUserCreds = new UserCreds(UserDataCreation.email, "45");
        userRequest
                .login(incorrectUserCreds).statusCode(401)
                .assertThat().body("message", is("email or password are incorrect"));
    }

    @Test
    public void loginWithIncorrectEmail() {
        incorrectUserCreds = new UserCreds("45", UserDataCreation.password);
        userRequest
                .login(incorrectUserCreds).statusCode(401)
                .assertThat().body("message", is("email or password are incorrect"));
    }

}

