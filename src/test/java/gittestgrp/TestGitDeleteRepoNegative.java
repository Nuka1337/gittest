package gittestgrp;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import static gittestgrp.TestGitConfig.*;
import static gittestgrp.TestGit.*;
import static org.junit.Assert.*;

public class TestGitDeleteRepoNegative {

    @BeforeClass
    public static void auth() {
        RestAssured.authentication = RestAssured.oauth2(TOKEN);
    }

    @BeforeClass
    public static void checkAuthentication() {
        TestGit.checkAuthentication();
        System.out.println(">> TestGitDeleteRepoNegative <<");
    }

    @Test
    public void test1DeleteNonExistentRepo() {
        System.out.println("> test1DeleteNonExistentRepo <");
        Response response = deleteGitRepo("nukadsjdnwjas122c", 404);
        assertEquals("Non-existent repo delete - Fail", "Not Found", response.jsonPath().getString("message"));
        System.out.println("Non-existent repo delete - Success");
    }
}
