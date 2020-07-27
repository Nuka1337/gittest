package gittestgrp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestGitCreateRepoNegative.class,
        TestGitCreateRepoPositive.class,
        TestGitDeleteRepoNegative.class,
        TestGitDeleteRepoPositive.class,
        TestGitGetRepositoryListNegative.class,
        TestGitGetRepositoryListPositive.class
})

public class TestGitSuite {
}
