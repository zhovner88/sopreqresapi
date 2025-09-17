package com.reqres.tests;

import com.reqres.api.model.UserResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ChainedRequestTests extends BaseApiTest {

    @Test
    void shouldFindUserById() {
        Integer expected = 2;
        UserResponse userResponse = defaultApiService.findUserById(expected);
        Integer actual = userResponse.getData().getId();

        Assert.assertEquals(actual, expected, "Found user should have ID 2");
    }

    @Test
    void shouldFindUserByEmail() {
        String expected = "janet.weaver@reqres.in";
        UserResponse userResponse = defaultApiService.findUserByEmail(expected);
        String actual = userResponse.getData().getEmail();

        Assert.assertEquals(actual, expected, "Found user should have correct email");
    }

    @Test
    void shouldFindUserByFirstName() {
        String expected = "Janet";
        UserResponse userResponse = defaultApiService.findUserByFirstName(expected);
        String actual = userResponse.getData().getFirstName();

        Assert.assertEquals(actual, expected, "Found user should have first name Janet");
    }

    @Test
    void shouldFindUserByLastName() {
        String expected = "Weaver";
        UserResponse userResponse = defaultApiService.findUserByLastName(expected);
        String actual = userResponse.getData().getLastName();

        Assert.assertEquals(actual, expected, "Found user should have last name Weaver");
    }
}