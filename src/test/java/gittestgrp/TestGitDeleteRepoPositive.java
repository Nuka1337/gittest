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

public class TestGitDeleteRepoPositive {

    static String test1RepoName = "TestRepo1";

    @BeforeClass
    public static void auth() {
        RestAssured.authentication = RestAssured.oauth2(TOKEN);
    }

    @BeforeClass
    public static void checkAuthentication() {
        TestGit.checkAuthentication();
        System.out.println(">> TestGitDeleteRepoPositive <<");
    }

    @Test
    public void test1DeleteGitRepo() {
        System.out.println("> test1DeleteGitRepo <");
        //создание тестового репозитория
        //создаем хэшмап для параметров запроса
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", test1RepoName);
        //создаем репозиторий
        createGitRepo(params, 201);
        //проверяем список репозиториев
        Response getRepoResponse = getGitResponse(GIT_URL+"/user/repos", 200);
        assertFalse("Response is null", getRepoResponse.getBody().toString().isEmpty());
        List<String> list = getRepoResponse.jsonPath().getList("name");
        if (list.contains(test1RepoName)) {
            //Удаляем репозиторий
            System.out.println("Check created repo - Success");
            //удаление репозитория после проверки
            deleteGitRepo(test1RepoName, 204);
        } else {
            fail("Check created repo - Fail");
        }
        //проверям что репозитория нет в списке
        //костыль с циклом тут из-за того что git ассинхронный и не сразу выдает список репозиториев без удалённого
        for (int i = 0; i < 10; i++) {
            //System.out.println(i);
            Response getRepoResponse2 = getGitResponse(GIT_URL+"/user/repos", 200);
            List<String> list2 = getRepoResponse2.jsonPath().getList("name");
            if (!list2.contains(test1RepoName)) {
                System.out.println("Check that repo deleted - Success");
                break;
            }
            if (i == 9) {
                fail("lol");
            }
        }
    }

}
