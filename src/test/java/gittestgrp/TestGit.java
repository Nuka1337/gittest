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

    final static String token = "putCorrectTokenHere"; //Нужно поменять на актуальный из письма

    public String generateRepoName() {
        return "Repo" + DateTimeFormatter.ofPattern("HHmmssddMMyyyy").format(LocalDateTime.now()) + "r" + (new Random().nextInt(1000) * new Random().nextInt(1000));
    }

    @BeforeClass
    public static void auth() {
        RestAssured.authentication = RestAssured.oauth2(token);
        int code = RestAssured.
                given().
                when().
                get(giturl+"/user").
                getStatusCode();
        assertEquals("Status not OK", 200, code);
    }

    //Получение списка репозиториев
    @Test
    public void test1() {
        int code = RestAssured.
                given().
                when().
                get(giturl+"/user/repos").
                getStatusCode();
        assertEquals("Status not OK 200", 200, code);
    }

    //Получение списка репозиториев с неккоректными параметрами запроса
    @Test
    public void test2() {
        int code = RestAssured.
                given().
                when().
                get(giturl+"/user/repos?visibility=private&type=private").
                getStatusCode();
        assertEquals("Not an unprocessable entry", 422, code);
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
        assertEquals("Status not Created 201", 201, code);
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
        assertEquals("Repo not created", 201, code);
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
        if (list.contains(reponame)) {
            int code2 = RestAssured.
                    given().
                    delete(giturl+"/repos/"+un+"/"+reponame).
                    getStatusCode();
            assertEquals("Repo not deleted", 204, code2);
        } else {
            fail("Repo not created");
        }
    }

    //Удаление несуществующего репозитория
    @Test
    public void test6() {
        int code = RestAssured.
                given().
                when().
                delete(giturl+"/repos/"+un+"/nukadsjdnwjas122c").
                getStatusCode();
        assertEquals("Repo deleted", 404, code);
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
