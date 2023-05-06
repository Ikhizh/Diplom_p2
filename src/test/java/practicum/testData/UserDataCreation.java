package practicum.testData;

import com.github.javafaker.Faker;

public class UserDataCreation {
    static Faker faker = new Faker();

    public static String name = faker.name().username();
    public static String email = faker.internet().emailAddress();
    public static String password = faker.internet().password();
}
