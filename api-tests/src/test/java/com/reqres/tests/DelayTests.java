package com.reqres.tests;

import com.reqres.api.model.UsersResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.reqres.api.conditions.Conditions.bodyField;
import static com.reqres.api.conditions.Conditions.statusCode;
import static org.hamcrest.Matchers.*;

public class DelayTests extends BaseApiTest {

    @DataProvider(name = "delayValues")
    public Object[] delayValues() {
        return new Object[] {1, 3, 5};
    }

    @Test(dataProvider = "delayValues")
    void shouldRespectDelayTiming(int delaySeconds) {
        long startTime = System.currentTimeMillis();

        defaultApiService.getUsersWithDelay(delaySeconds)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("data", notNullValue()))
                .shouldHave(bodyField("data", not(empty())));

        long endTime = System.currentTimeMillis();
        long actualDelay = (endTime - startTime) / 1000;

        Assert.assertTrue(actualDelay >= delaySeconds,
                String.format("Response should take at least %d seconds, but took %d seconds", delaySeconds, actualDelay));
        Assert.assertTrue(actualDelay <= delaySeconds + 2,
                String.format("Response should not exceed %d seconds (SLA), but took %d seconds", delaySeconds + 2, actualDelay));
    }

    @Test
    void shouldMaintainDataIntegrityWithDelay() {
        UsersResponse normalResponse = defaultApiService.getUsersFromPage(1)
                .shouldHave(statusCode(200))
                .as(UsersResponse.class);

        UsersResponse delayedResponse = defaultApiService.getUsersWithDelay(2)
                .shouldHave(statusCode(200))
                .as(UsersResponse.class);

        Assert.assertEquals(delayedResponse.getTotal(), normalResponse.getTotal(),
                "Total count should be same for delayed and normal responses");
        Assert.assertEquals(delayedResponse.getData().size(), normalResponse.getData().size(),
                "Data size should be same for delayed and normal responses");
    }

    @Test
    void shouldHandleZeroDelayCorrectly() {
        defaultApiService.getUsersWithDelay(0)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("data", notNullValue()));
    }

    @Test
    void shouldReturnCorrectResponseStructureWithDelay() {
        defaultApiService.getUsersWithDelay(2)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("page", notNullValue()))
                .shouldHave(bodyField("per_page", notNullValue()))
                .shouldHave(bodyField("total", notNullValue()))
                .shouldHave(bodyField("total_pages", notNullValue()))
                .shouldHave(bodyField("data", notNullValue()));
    }

    @Test
    void shouldHandleNegativeDelay() {
        defaultApiService.getUsersWithDelay(-1)
                .shouldHave(statusCode(200))
                .shouldHave(bodyField("data", notNullValue()));
    }

}