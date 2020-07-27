package gittestgrp;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import static gittestgrp.TestGit.*;
import static gittestgrp.TestGitConfig.*;


public class TestGitGetRepositoryListNegative {

    @BeforeClass
    public static void auth() {
        RestAssured.authentication = RestAssured.oauth2(TOKEN);
    }

    @BeforeClass
    public static void checkAuthentication() {
        TestGit.checkAuthentication();
        System.out.println(">> TestGitGetRepositoryListNegative <<");
    }

    @Test
    public void test1GetRepositoryListWithParamError() {
        System.out.println("> test1GetRepositoryListWithParamError <");
        Response response = getGitResponse(GIT_URL+"/user/repos?visibility=private&type=private", 422);
        assertEquals("Not an unprocessable entry", 422, response.getStatusCode());
        System.out.println("Get repo list with param error - Success");
    }

}
