package com.reqres.tests;

import com.reqres.api.model.User;
import com.reqres.api.model.UsersResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.reqres.api.conditions.Conditions.bodyField;
import static com.reqres.api.conditions.Conditions.statusCode;
import static org.hamcrest.Matchers.*;

public class PaginationTests extends BaseApiTest {

    @Test
    void shouldReturnCorrectPaginationFieldsForPage2() {
        defaultApiService.getUsersFromPage(2)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("page", equalTo(2)))
                .shouldHave(bodyField("per_page", equalTo(6)))
                .shouldHave(bodyField("total", greaterThan(0)))
                .shouldHave(bodyField("total_pages", greaterThan(0)))
                .shouldHave(bodyField("data", notNullValue()));
    }

    @Test
    void shouldReturnCorrectTotalPagesCount() {
        UsersResponse response = defaultApiService.getUsersFromPage(1)
                .shouldHave(statusCode(200))
                .as(UsersResponse.class);

        int totalUsers = response.getTotal();
        int usersPerPage = response.getPerPage();
        int expectedPages = totalUsers / usersPerPage;
        if (totalUsers % usersPerPage > 0) {
            expectedPages++;
        }

        Assert.assertEquals(response.getTotalPages(), Integer.valueOf(expectedPages),
                String.format("With %d users and %d per page, should have %d pages",
                        totalUsers, usersPerPage, expectedPages));
    }

    @Test
    void shouldReturnCorrectNumberOfUsersOnPage() {
        UsersResponse response = defaultApiService.getUsersFromPage(2)
                .shouldHave(statusCode(200))
                .as(UsersResponse.class);

        Assert.assertTrue(response.getData().size() <= response.getPerPage(),
                String.format("Page should not have more than %d users, but got %d",
                        response.getPerPage(), response.getData().size()));
    }

    @Test
    void shouldValidateUserDataStructure() {
        defaultApiService.getUsersFromPage(1)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("data[0].id", notNullValue()))
                .shouldHave(bodyField("data[0].email", notNullValue()))
                .shouldHave(bodyField("data[0].first_name", notNullValue()))
                .shouldHave(bodyField("data[0].last_name", notNullValue()))
                .shouldHave(bodyField("data[0].avatar", notNullValue()));
    }

    @Test
    void shouldMaintainMetadataConsistencyAcrossPages() {
        UsersResponse page1 = defaultApiService.getUsersFromPage(1)
                .shouldHave(statusCode(200))
                .as(UsersResponse.class);

        UsersResponse page2 = defaultApiService.getUsersFromPage(2)
                .shouldHave(statusCode(200))
                .as(UsersResponse.class);

        Assert.assertEquals(page1.getTotal(), page2.getTotal(),
                "Total count should be consistent across pages");
        Assert.assertEquals(page1.getTotalPages(), page2.getTotalPages(),
                "Total pages should be consistent across pages");
        Assert.assertEquals(page1.getPerPage(), page2.getPerPage(),
                "Per page should be consistent across pages");
    }

    @Test
    void shouldEnsureUniqueUsersAcrossPages() {
        UsersResponse page1 = defaultApiService.getUsersFromPage(1)
                .shouldHave(statusCode(200))
                .as(UsersResponse.class);

        UsersResponse page2 = defaultApiService.getUsersFromPage(2)
                .shouldHave(statusCode(200))
                .as(UsersResponse.class);

        boolean hasIntersection = page1.getData().stream()
                .map(User::getId)
                .anyMatch(id -> page2.getData().stream()
                        .map(User::getId)
                        .collect(java.util.stream.Collectors.toSet())
                        .contains(id));

        Assert.assertFalse(hasIntersection, "Users should be unique across pages");
    }

    @Test
    void shouldHandleInvalidPageRequests() {
        UsersResponse validResponse = defaultApiService.getUsersFromPage(1)
                .shouldHave(statusCode(200))
                .as(UsersResponse.class);

        int invalidPage = validResponse.getTotalPages() + 1;

        UsersResponse emptyResponse = defaultApiService.getUsersFromPage(invalidPage)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("page", equalTo(invalidPage)))
                .shouldHave(bodyField("data", empty()))
                .as(UsersResponse.class);

        Assert.assertEquals(emptyResponse.getPage(), Integer.valueOf(invalidPage),
                "API should return requested page number even when beyond range");
        Assert.assertTrue(emptyResponse.getData().isEmpty(),
                "Data should be empty for page beyond range");
    }
}