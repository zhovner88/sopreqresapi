package com.reqres.tests;

import com.reqres.api.model.UserResponse;
import org.testng.annotations.Test;

import static com.reqres.api.conditions.Conditions.bodyField;
import static com.reqres.api.conditions.Conditions.statusCode;
import static org.hamcrest.Matchers.*;

public class UserTests extends BaseApiTest {

    @Test
    void shouldGetUserByIdSuccessfully() {
        defaultApiService.getUserById(2)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("data.id", equalTo(2)))
                .shouldHave(bodyField("data.email", notNullValue()))
                .shouldHave(bodyField("data.first_name", notNullValue()))
                .shouldHave(bodyField("data.last_name", notNullValue()))
                .shouldHave(bodyField("data.avatar", notNullValue()))
                .as(UserResponse.class);
    }

    @Test
    void shouldValidateUserDataStructure() {
        defaultApiService.getUserById(2)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("data", notNullValue()))
                .shouldHave(bodyField("data.id", instanceOf(Integer.class)))
                .shouldHave(bodyField("data.email", instanceOf(String.class)))
                .shouldHave(bodyField("data.first_name", instanceOf(String.class)))
                .shouldHave(bodyField("data.last_name", instanceOf(String.class)))
                .shouldHave(bodyField("data.avatar", instanceOf(String.class)));
    }

    @Test
    void shouldValidateEmailFormat() {
        defaultApiService.getUserById(2)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("data.email", matchesPattern("^[\\w._%+-]+@reqres.in")));
    }

    @Test
    void shouldValidateAvatarUrl() {
        defaultApiService.getUserById(2)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField(
                        "data.avatar",
                        matchesPattern("^https://reqres\\.in/img/faces/.*\\.jpg$"))
                );
    }

    @Test
    void shouldReturnNotFoundForNonExistentUser() {
        defaultApiService.getUserById(33)
                .shouldHave(statusCode(404));
    }
}