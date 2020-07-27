package gittestgrp;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static gittestgrp.TestGit.*;
import static gittestgrp.TestGitConfig.*;
import static org.junit.Assert.*;

public class TestGitCreateRepoNegative {

    @BeforeClass
    public static void auth() {
        RestAssured.authentication = RestAssured.oauth2(TOKEN);
    }

    @BeforeClass
    public static void checkAuthentication() {
        TestGit.checkAuthentication();
        System.out.println(">> TestGitCreateRepoNegative <<");
    }

    @Test
    public void test1CreateGitRepoWithNullBody() {
        System.out.println("> test1CreateGitRepoWithNullBody <");
        //создаем репозиторий
        Response response = createGitRepo(400);
        assertEquals("Message incorrect","Body should be a JSON object", response.getBody().jsonPath().getString("message"));
        System.out.println("Null body repo creation request test - Success");
    }

    @Test
    public void test2CreateGitRepoWithIncorrectBody() {
        System.out.println("> test2CreateGitRepoWithIncorrectBody <");
        //создаем пустой хэшмап для параметров запроса
        HashMap<String, Object> params = new HashMap<>();
        //создаем репозиторий
        Response response = createGitRepo(params, 422);
        assertEquals("Message incorrect","Repository creation failed.", response.getBody().jsonPath().getString("message"));
        System.out.println("Repo with incorrect params creation request test - Success");
    }

}
