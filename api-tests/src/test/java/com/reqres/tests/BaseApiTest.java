package com.reqres.tests;

import com.reqres.api.services.DefaultApiService;
import common.Constants;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {

    protected final DefaultApiService  defaultApiService = new DefaultApiService();


    @BeforeClass
    static void setup() {
        RestAssured.baseURI = Constants.BASE_URI;
    }
}