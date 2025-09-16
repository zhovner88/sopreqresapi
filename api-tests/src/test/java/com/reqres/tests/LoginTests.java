package com.reqres.tests;

import com.reqres.api.model.LoginRequest;
import com.reqres.api.model.LoginResponse;
import com.reqres.api.model.LoginResponseError;
import org.testng.annotations.Test;

import static com.reqres.api.conditions.Conditions.bodyField;
import static com.reqres.api.conditions.Conditions.statusCode;
import static org.hamcrest.Matchers.*;

public class LoginTests extends BaseApiTest {

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        LoginRequest request = new LoginRequest()
                .setEmail("eve.holt@reqres.in")
                .setPassword("cityslicka");

        defaultApiService.login(request)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("token", notNullValue()))
                .shouldHave(bodyField("token", matchesPattern("^[A-Za-z0-9]+$")))
                .as(LoginResponse.class);
    }

    @Test
    void shouldFailLoginWithInvalidCredentials() {
        LoginRequest request = new LoginRequest()
                .setEmail("holt.eve@reqres.in")
                .setPassword("cityslicka");

        defaultApiService.login(request)
                .shouldHave(statusCode(400))
                .shouldHave(bodyField("error", equalTo("user not found")))
                .as(LoginResponseError.class);
    }

    @Test
    void shouldFailLoginWithMissingPassword() {
        LoginRequest request = new LoginRequest()
                .setEmail("eve.holt@reqres.in");

        defaultApiService.login(request)
                .shouldHave(statusCode(400))
                .shouldHave(bodyField("error", equalTo("Missing password")))
                .as(LoginResponseError.class);
    }

    @Test
    void shouldFailLoginWithMissingEmail() {
        LoginRequest request = new LoginRequest()
                .setPassword("cityslicka");

        defaultApiService.login(request)
                .shouldHave(statusCode(400))
                .shouldHave(bodyField("error", equalTo("Missing email or username")))
                .as(LoginResponseError.class);
    }

    @Test
    void shouldFailLoginWithEmptyCredentials() {
        LoginRequest request = new LoginRequest()
                .setEmail("")
                .setPassword("");

        defaultApiService.login(request)
                .shouldHave(statusCode(400))
                .shouldHave(bodyField("error", equalTo("Missing email or username")))
                .as(LoginResponseError.class);
    }
}
