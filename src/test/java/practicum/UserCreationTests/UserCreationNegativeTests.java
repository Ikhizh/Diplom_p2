package practicum.UserCreationTests;

import org.junit.Before;
import org.junit.Test;
import practicum.requests.UserRequest;
import practicum.testData.UserDataCreation;
import practicum.user.User;
import practicum.user.UserCreds;

import static org.hamcrest.Matchers.is;

public class UserCreationNegativeTests {
    private User user;
    private UserRequest userRequest;
    private UserCreds userCreds;

    @Before
    public void setUp() {
        userRequest = new UserRequest();
        userCreds = new UserCreds(UserDataCreation.email, UserDataCreation.password);
    }

    @Test
    public void createUserWithoutEmail() {
        user = new User(null, UserDataCreation.password, UserDataCreation.name);
        userRequest
                .create(user)
                .statusCode(403)
                .assertThat().body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutPassword() {
        user = new User(UserDataCreation.email, null, UserDataCreation.name);
        userRequest
                .create(user)
                .statusCode(403)
                .assertThat().body("success", is(false))
                .body("message", is("Email, password and name are required fields"));

    }

    @Test
    public void createUserWithoutName() {
        user = new User(UserDataCreation.email, UserDataCreation.password, null);
        userRequest
                .create(user)
                .statusCode(403)
                .assertThat().body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

}