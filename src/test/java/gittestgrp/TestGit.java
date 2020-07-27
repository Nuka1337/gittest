package gittestgrp;

import io.restassured.RestAssured;
import static org.junit.Assert.*;

import io.restassured.response.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import java.util.Random;
import static gittestgrp.TestGitConfig.*;

public class TestGit {

    public static String generateRepoName() {
        return "Repo" + DateTimeFormatter.ofPattern("HHmmssddMMyyyy").format(LocalDateTime.now()) + "r" + (new Random().nextInt(1000) * new Random().nextInt(1000));
    }

    public static void checkAuthentication() {
        Response response = getGitResponse(GIT_URL+"/user", 200);
        assertEquals("Login is not right", USER_NAME, response.jsonPath().getString("login"));
        System.out.println("Auth - Success");
    }

    public static Response getGitResponse (String url, int expectedStatusCode) throws AssertionError {
        return RestAssured.
                given().
                when().
                get(url).
                then().
                statusCode(expectedStatusCode).
                contentType("application/json").
                extract().
                response();
    }

    public static Response createGitRepo (HashMap<String, Object> params, int expectedStatusCode) throws AssertionError {
        return RestAssured.
                given().
                contentType("application/json").
                accept("application/json").
                when().
                body(params).
                post(GIT_URL+"/user/repos").
                then().
                statusCode(expectedStatusCode).
                contentType("application/json").
                extract().
                response();
    }

    public static Response createGitRepo (int expectedStatusCode) throws AssertionError {
        return RestAssured.
                given().
                contentType("application/json").
                accept("application/json").
                when().
                post(GIT_URL+"/user/repos").
                then().
                statusCode(expectedStatusCode).
                contentType("application/json").
                extract().
                response();
    }

    public static Response deleteGitRepo (String name, int expectedStatusCode) throws AssertionError {
        return RestAssured.
                given().
                when().
                delete(GIT_URL+"/repos/"+USER_NAME+"/"+name).
                then().
                statusCode(expectedStatusCode).
                extract().
                response();
    }

    public static Response deleteGitRepo (String name) throws AssertionError {
        return RestAssured.
                given().
                when().
                delete(GIT_URL+"/repos/"+USER_NAME+"/"+name).
                then().
                extract().
                response();
    }

}
