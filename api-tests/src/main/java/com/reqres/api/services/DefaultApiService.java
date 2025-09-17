package com.reqres.api.services;

import com.reqres.api.AssertableResponse;
import com.reqres.api.model.LoginRequest;
import com.reqres.api.model.RegisterRequest;
import com.reqres.api.model.User;
import com.reqres.api.model.UserResponse;
import com.reqres.api.model.UsersResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.reqres.api.conditions.Conditions.statusCode;

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

    public AssertableResponse getUsersWithDelay(int delaySeconds) {
        log.info("Get users with delay: {} seconds", delaySeconds);

        return new AssertableResponse(setUpRequest()
                .queryParam("delay", delaySeconds)
                .when()
                .get("api/users")
                .then());
    }

    private <T> UserResponse findUserByField(Function<User, T> fieldSelector, T searchValue) {
        log.info("Finding user by field value: {}", searchValue);

        int usersPerPage = getUsersFromPage(1)
                .shouldHave(statusCode(200))
                .as(UsersResponse.class)
                .getPerPage();

        List<User> allUsers = new ArrayList<>();
        int page = 1;

        while (true) {
            UsersResponse response = getUsersFromPage(page)
                    .shouldHave(statusCode(200))
                    .as(UsersResponse.class);

            allUsers.addAll(response.getData());

            if (response.getData().size() < usersPerPage) {
                break;
            }
            page++;
        }

        User foundUser = allUsers.stream()
                .filter(user -> Objects.equals(fieldSelector.apply(user), searchValue))
                .findFirst()
                .orElseThrow(() -> new AssertionError("User not found with field value: " + searchValue));

        return getUserById(foundUser.getId())
                .shouldHave(statusCode(200))
                .as(UserResponse.class);
    }

    public UserResponse findUserByEmail(String email) {
        return findUserByField(User::getEmail, email);
    }

    public UserResponse findUserByFirstName(String firstName) {
        return findUserByField(User::getFirstName, firstName);
    }

    public UserResponse findUserByLastName(String lastName) {
        return findUserByField(User::getLastName, lastName);
    }

    public UserResponse findUserById(Integer id) {
        return findUserByField(User::getId, id);
    }
}
