package com.reqres.tests;

import com.reqres.api.model.RegisterRequest;
import com.reqres.api.model.RegisterResponse;
import com.reqres.api.model.RegisterResponseError;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.reqres.api.conditions.Conditions.bodyField;
import static com.reqres.api.conditions.Conditions.statusCode;
import static org.hamcrest.Matchers.*;

public class RegisterTests extends BaseApiTest {

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest()
                .setEmail("eve.holt@reqres.in")
                .setPassword("cityslicka");

        defaultApiService.register(request)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("id", notNullValue()))
                .shouldHave(bodyField("token", notNullValue()))
                .as(RegisterResponse.class);
    }

    @DataProvider(name = "invalidRegisterData")
    public Object[][] invalidRegisterData() {
        return new Object[][] {
                {"eve.holt@reqres.in", null, "Missing password"},
                {"", "cityslicka", "Missing email or username"},
                {"invalid@email.com", "cityslicka", "Note: Only defined users succeed registration"}
        };
    }

    @Test(dataProvider = "invalidRegisterData")
    void shouldFailRegisterWithInvalidData(String email, String password, String expectedError) {
        RegisterRequest request = new RegisterRequest()
                .setEmail(email)
                .setPassword(password);

        defaultApiService.register(request)
                .shouldHave(statusCode(400))
                .shouldHave(bodyField("error", equalTo(expectedError)))
                .as(RegisterResponseError.class);
    }
}