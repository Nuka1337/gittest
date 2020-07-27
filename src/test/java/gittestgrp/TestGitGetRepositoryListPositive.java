package gittestgrp;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

import static gittestgrp.TestGit.*;
import static gittestgrp.TestGitConfig.*;


public class TestGitGetRepositoryListPositive {

    @BeforeClass
    public static void auth() {
        RestAssured.authentication = RestAssured.oauth2(TOKEN);
    }

    @BeforeClass
    public static void checkAuthentication() {
        TestGit.checkAuthentication();
        System.out.println(">> TestGitGetRepositoryListPositive <<");
    }

    //Получение списка репозиториев
    @Test
    public void test1GetRepositoryList() {
        System.out.println("> test1GetRepositoryList <");
        Response response = getGitResponse(GIT_URL+"/user/repos", 200);
        assertFalse("Response is null", response.getBody().toString().isEmpty());
        System.out.println("Get repositories - Success");
    }

    //Получение списка репозиториев с параметрами
    @Test
    public void test2GetRepositoryListWithPreCreatedRepo() {
        System.out.println("> test2GetRepositoryListWithPreCreatedRepo <");
        //создаем хэшмап для параметров запроса
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", "PreCreatedRepo");
        //создание репозитория для проверки
        createGitRepo(params, 201);
        System.out.println("Test repo create - Success");
        Response response = getGitResponse(GIT_URL+"/user/repos?visibility=public", 200);
        assertFalse("Response is null", response.getBody().toString().isEmpty());
        System.out.println("Get repo list - Success");
        List<String> list = response.jsonPath().getList("name");
        if (list.contains("PreCreatedRepo")) {
            System.out.println("Get created repo - Success");
            //удаление репозитория для проверки
            deleteGitRepo("PreCreatedRepo", 204);
            System.out.println("Delete created repo - Success");
        } else {
            fail("Failed to get PreCreatedRepo");
        }
    }

}
