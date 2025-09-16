package com.reqres.api.services;

import com.reqres.api.AssertableResponse;
import com.reqres.api.model.LoginRequest;
import com.reqres.api.model.RegisterRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultApiService extends BaseApiService {

    public AssertableResponse register(RegisterRequest request) {
        log.info("Register user with email: {}", request.getEmail());

        return new AssertableResponse(setUpRequest()
                .body(request)
                .when()
                .post("api/register")
                .then());
    }

    public AssertableResponse login(LoginRequest request) {
        log.info("Login user with email: {}", request.getEmail());

        return new AssertableResponse(setUpRequest()
                .body(request)
                .when()
                .post("api/login")
                .then());
    }

    public AssertableResponse getUserById(int userId) {
        log.info("Get user by ID: {}", userId);

        return new AssertableResponse(setUpRequest()
                .when()
                .get("api/users/" + userId)
                .then());
    }

    public AssertableResponse getUsersFromPage(int page) {
        log.info("Get users - page: {}", page);

        return new AssertableResponse(setUpRequest()
                .queryParam("page", page)
                .when()
                .get("api/users")
                .then());
    }
}
