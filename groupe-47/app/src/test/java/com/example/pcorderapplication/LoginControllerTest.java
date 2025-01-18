package com.example.pcorderapplication;

import android.content.Context;

import com.example.pcorderapplication.controller.LoginController;
import com.example.pcorderapplication.model.database.UserRepository;
import com.example.pcorderapplication.model.entity.UserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class LoginControllerTest {

    @Mock
    Context mockContext;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    FirebaseAuth mockFirebaseAuth;

    @Mock
    FirebaseUser mockFirebaseUser;

    @Mock
    UserInfo mockUserInfo;

    private LoginController loginController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        loginController = new LoginController(mockContext, mockUserRepository, mockFirebaseAuth);
    }

    @Test
    public void testLogin_Success() {
        String email = "user@example.com";
        String password = "password123";

        // Simule une tâche réussie
        com.google.android.gms.tasks.Task<AuthResult> mockSignInTask = mockTask(true);

        when(mockFirebaseAuth.signInWithEmailAndPassword(eq(email), eq(password))).thenReturn(mockSignInTask);
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        when(mockFirebaseUser.getEmail()).thenReturn(email);
        when(mockUserRepository.findUserByEmail(email)).thenReturn(mockUserInfo);

        loginController.login(email, password, new LoginController.OnLoginResultListener() {
            @Override
            public void onLoginSuccess(UserInfo user) {
                assertEquals(mockUserInfo, user);
            }

            @Override
            public void onLoginFailure(String errorMessage) {
                fail("Login should succeed.");
            }
        });
    }


    @Test
    public void testLogin_Failure() {
        String email = "user@example.com";
        String password = "password123";
        com.google.android.gms.tasks.Task<AuthResult> mockSignInTask = mockTask(false);

        when(mockFirebaseAuth.signInWithEmailAndPassword(eq(email), eq(password))).thenReturn(mockSignInTask);
        loginController.login(email, password, new LoginController.OnLoginResultListener() {
            @Override
            public void onLoginSuccess(UserInfo user) {
                fail("Login should fail.");
            }

            @Override
            public void onLoginFailure(String errorMessage) {
                assertNotNull(errorMessage);
            }
        });
    }

    private com.google.android.gms.tasks.Task<AuthResult> mockTask(boolean isSuccess) {
        com.google.android.gms.tasks.Task<AuthResult> mockTask = mock(com.google.android.gms.tasks.Task.class);
        AuthResult mockAuthResult = mock(AuthResult.class);

        when(mockTask.isSuccessful()).thenReturn(isSuccess);

        if (isSuccess) {
            when(mockTask.getResult()).thenReturn(mockAuthResult);
        }

        doAnswer(invocation -> {
            com.google.android.gms.tasks.OnCompleteListener<AuthResult> listener = invocation.getArgument(0);
            listener.onComplete(mockTask);
            return mockTask;
        }).when(mockTask).addOnCompleteListener(any());

        return mockTask;
    }

}