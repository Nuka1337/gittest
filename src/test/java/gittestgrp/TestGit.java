package gittestgrp;

import io.restassured.RestAssured;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TestGit {

    final static String un = "nukagittest";
    final static String giturl = "https://api.github.com";

    public String generateRepoName() {
        return "Repo" + DateTimeFormatter.ofPattern("HHmmssddMMyyyy").format(LocalDateTime.now()) + "r" + (new Random().nextInt(1000) * new Random().nextInt(1000));
    }

    @BeforeClass
    public static void auth() {
        RestAssured.authentication = RestAssured.oauth2("9acadfa5935ee9d3ca90eaa831493c3d93b368ab");
        int code = RestAssured.
                given().
                when().
                get(giturl+"/user").
                getStatusCode();
         assertTrue("Status not OK", code == 200);
    }

    //Получение списка репозиториев
    @Test
    public void test1() {
        int code = RestAssured.
                given().
                when().
                get(giturl+"/user/repos").
                getStatusCode();
        assertTrue("Status not OK 200", code == 200);
    }

    //Получение списка репозиториев с неккоректными параметрами запроса
    @Test
    public void test2() {
        int code = RestAssured.
                given().
                when().
                get(giturl+"/user/repos?visibility=private&type=private").
                getStatusCode();
        assertTrue("Not an unprocessable entry", code == 422);
    }

    //Создание репозитория
    @Test
    public void test3() {
        Map<String, String> jsonparams = new HashMap<>();
        jsonparams.put("name", generateRepoName());
        int code = RestAssured.
                given().
                contentType("application/json").
                accept("application/json").
                when().
                body(jsonparams).
                post(giturl+"/user/repos").
                getStatusCode();
        assertTrue("Status not Created 201", code == 201);
    }

    //Создание репозитория с ошибкой
    @Test
    public void test4() {
        int code = RestAssured.
                given().
                contentType("application/json").
                accept("application/json").
                when().
                body("null").
                post(giturl+"/user/repos").
                getStatusCode();
        assertTrue("Status not Created 201", code != 201);
    }

    //Удаление репозитория
    @Test
    public void test5() {
        String reponame = generateRepoName();
        Map<String, String> jsonparams = new HashMap<>();
        jsonparams.put("name", reponame);
        int code = RestAssured.
                given().
                contentType("application/json").
                accept("application/json").
                when().
                body(jsonparams).
                post(giturl+"/user/repos").
                getStatusCode();
        assertTrue("Repo not created", code == 201);
        int code2 = RestAssured.
                given().
                when().
                delete(giturl+"/repos/"+un+"/"+reponame).
                getStatusCode();
        assertTrue("Repo not deleted", code2 == 204);
    }

    //Удаление несуществующего репозитория
    @Test
    public void test6() {
        int code = RestAssured.
                given().
                when().
                delete(giturl+"/repos/"+un+"/nukadsjdnwjas122c").
                getStatusCode();
        assertTrue("Repo deleted", code == 404);
    }

    //Удаление всех репозиториев
    @AfterClass
    public static void deleteAllRepos(){
        Response response = RestAssured.
                given().
                when().
                get(giturl+"/user/repos").
                then().
                statusCode(200).
                contentType("application/json").
                extract().
                response();
        List<String> list = response.jsonPath().getList("name");
        for(String name : list) {
            RestAssured.
                    given().
                    delete(giturl+"/repos/"+un+"/"+name);
        }
    }

}