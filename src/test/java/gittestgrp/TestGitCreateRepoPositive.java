package gittestgrp;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static gittestgrp.TestGit.*;
import static gittestgrp.TestGitConfig.*;
import static org.junit.Assert.*;

public class TestGitCreateRepoPositive {

    static String test1RepoName = "TestRepo1";

    @BeforeClass
    public static void auth() {
        RestAssured.authentication = RestAssured.oauth2(TOKEN);
    }

    @BeforeClass
    public static void checkAuthentication() {
        TestGit.checkAuthentication();
        System.out.println(">> TestGitCreateRepoPositive <<");
    }

    @Test
    public void test1CreateGitRepo () {
        System.out.println("> test1CreateGitRepo <");
        //удаляем репозиторий с тестовым именем если он создан
        deleteGitRepo(test1RepoName);
        //создаем хэшмап для параметров запроса
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", test1RepoName);
        //создаем репозиторий
        Response response = createGitRepo(params, 201);
        assertFalse("Response is null", response.getBody().toString().isEmpty());
        if (response.jsonPath().getString("name").equals(test1RepoName)) {
            System.out.println("Create repo - Success");
        } else {
            fail("Failed to create repo "+test1RepoName);
        }
        //проверяем список репозиториев на наличие тестового
        Response getRepoResponse = getGitResponse(GIT_URL+"/user/repos", 200);
        assertFalse("Response is null", getRepoResponse.getBody().toString().isEmpty());
        List<String> list = getRepoResponse.jsonPath().getList("name");
        if (list.contains(test1RepoName)) {
            System.out.println("Check created repo - Success");
            //удаление репозитория после проверки
            deleteGitRepo(test1RepoName, 204);
            System.out.println("Delete created repo - Success");
        } else {
            fail("Failed to check "+test1RepoName);
        }
    }



}
