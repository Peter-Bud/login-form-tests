package com.example.logintests.api;

import com.example.logintests.support.HttpMethod;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public final class RestClient {

    private static final Logger LOGGER = LogManager.getLogger(RestClient.class);
    private final String baseUrl = ApiConfig.baseUrl();

    public Response post(String path, Object body) {
        Response response = given().baseUri(baseUrl).contentType(ContentType.JSON).accept(ContentType.JSON.toString())
            .body(body).post(path);
        return logResponse(HttpMethod.POST, path, response);
    }

    public Response get(String path) {
        Response response = given().baseUri(baseUrl).accept(ContentType.JSON.toString()).get(path);
        return logResponse(HttpMethod.GET, path, response);
    }

    public Response put(String path, Object body, String token) {
        Response response = given().baseUri(baseUrl).contentType(ContentType.JSON).accept(ContentType.JSON.toString())
            .cookie(ApiConstants.AUTH_COOKIE, token).body(body).put(path);
        return logResponse(HttpMethod.PUT, path, response);
    }

    public Response delete(String path, String token) {
        Response response = given().baseUri(baseUrl).cookie(ApiConstants.AUTH_COOKIE, token).delete(path);
        return logResponse(HttpMethod.DELETE, path, response);
    }

    private Response logResponse(HttpMethod method, String path, Response response) {
        LOGGER.info(ApiConstants.RESPONSE_LOG, method, path, response.statusCode());
        return response;
    }
}
