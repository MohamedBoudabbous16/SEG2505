package com.example.pcorderapplication;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdministratorControllerTest.class,
        AssemblerControllerTest.class,
        LoginControllerTest.class,
        MainControllerTest.class,
        RequesterControllerTest.class,
        StoreKeeperControllerTest.class
})
public class AllTests {

    public static void main(String[] args) {
        Class<?>[] testClasses = {
                AdministratorControllerTest.class,
                AssemblerControllerTest.class,
                LoginControllerTest.class,
                MainControllerTest.class,
                RequesterControllerTest.class,
                StoreKeeperControllerTest.class
        };

        JUnitCore junit = new JUnitCore();


        junit.addListener(new RunListener() {
            @Override
            public void testRunStarted(Description description) {
                System.out.println("Test execution started...");
                System.out.println("Total tests: " + description.testCount());
            }

            @Override
            public void testStarted(Description description) {
                System.out.println("\n--- Running test: " + description.getClassName() + " -> " + description.getMethodName());
            }

            @Override
            public void testFinished(Description description) {
                System.out.println("--- Test finished: " + description.getMethodName());
            }

            @Override
            public void testFailure(Failure failure) {
                System.out.println("!!! Test failed: " + failure.getDescription().getMethodName());
                System.out.println("Reason: " + failure.getMessage());
            }

            @Override
            public void testIgnored(Description description) {
                System.out.println("*** Test ignored: " + description.getMethodName());
            }
        });

        for (Class<?> testClass : testClasses) {
            System.out.println("\n==> Running tests for: " + testClass.getSimpleName());
            Result result = junit.run(testClass);

            System.out.println("\nResults for " + testClass.getSimpleName() + ":");
            System.out.println("Total tests executed: " + result.getRunCount());
            System.out.println("Tests passed: " + (result.getRunCount() - result.getFailureCount()));
            System.out.println("Tests failed: " + result.getFailureCount());
            if (!result.wasSuccessful()) {
                System.out.println("Failure details:");
                for (Failure failure : result.getFailures()) {
                    System.out.println(" - " + failure.getDescription().getMethodName() + ": " + failure.getMessage());
                }
            }
        }

        System.out.println("\n--- Test execution completed ---");
    }
}
