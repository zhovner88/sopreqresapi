package com.reqres.tests;

import com.reqres.api.model.RegisterRequest;
import com.reqres.api.model.RegisterResponse;
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
}