package com.reqres.tests;

import com.reqres.api.model.LoginRequest;
import com.reqres.api.model.LoginResponse;
import org.testng.annotations.Test;

import static com.reqres.api.conditions.Conditions.bodyField;
import static com.reqres.api.conditions.Conditions.statusCode;
import static org.hamcrest.Matchers.*;

public class LoginTests extends BaseApiTest {

    @Test
    void shouldValidateTokenFormat() {
        LoginRequest request = new LoginRequest()
                .setEmail("eve.holt@reqres.in")
                .setPassword("cityslicka");

        defaultApiService.login(request)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("token", matchesPattern("^[A-Za-z0-9]+$")))
                .as(LoginResponse.class);
    }
}
