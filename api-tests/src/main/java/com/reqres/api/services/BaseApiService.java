package com.reqres.api.services;

import common.Constants;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseApiService {

    public RequestSpecification setUpRequest() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("x-api-key", Constants.SPECIAL_API_KEY)
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new AllureRestAssured()
                );
    }

}
