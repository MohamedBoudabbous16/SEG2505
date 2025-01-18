package com.example.pcorderapplication;

import android.content.Context;
import android.content.Intent;

import com.example.pcorderapplication.controller.LoginController;
import com.example.pcorderapplication.controller.MainController;
import com.example.pcorderapplication.model.entity.UserInfo;
import com.example.pcorderapplication.view.AdministratorActivity;
import com.example.pcorderapplication.view.StoreKeeperActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class MainControllerTest {

    @Mock
    private Context mockContext;

    @Mock
    private LoginController mockLoginController;

    @Mock
    private UserInfo mockUserInfo;

    private MainController mainController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        mainController = new MainController(mockContext, mockLoginController);
    }

    @Test
    public void testNavigateToRoleActivity_Administrator() {
        when(mockLoginController.isUserLoggedIn()).thenReturn(true);
        when(mockLoginController.getCurrentUser()).thenReturn(mockUserInfo);
        when(mockUserInfo.getRole()).thenReturn("Administrator");

        doAnswer(invocation -> {
            LoginController.OnLoginResultListener listener = invocation.getArgument(2);
            listener.onLoginSuccess(mockUserInfo);
            return null;
        }).when(mockLoginController).login(anyString(), anyString(), any());

        mainController.loginUser("admin@example.com", "adminpass");

        verify(mockLoginController).login(eq("admin@example.com"), eq("adminpass"), any());
        verify(mockLoginController, times(1)).getCurrentUser();

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mockContext).startActivity(intentCaptor.capture());
        assertEquals(AdministratorActivity.class.getName(), intentCaptor.getValue().getComponent().getClassName());
    }

    @Test
    public void testNavigateToRoleActivity_StoreKeeper() {
        when(mockLoginController.isUserLoggedIn()).thenReturn(true);
        when(mockLoginController.getCurrentUser()).thenReturn(mockUserInfo);
        when(mockUserInfo.getRole()).thenReturn("StoreKeeper");

        doAnswer(invocation -> {
            LoginController.OnLoginResultListener listener = invocation.getArgument(2);
            listener.onLoginSuccess(mockUserInfo);
            return null;
        }).when(mockLoginController).login(anyString(), anyString(), any());

        mainController.loginUser("storekeeper@example.com", "password");

        verify(mockLoginController).login(eq("storekeeper@example.com"), eq("password"), any());
        verify(mockLoginController, times(1)).getCurrentUser();

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mockContext).startActivity(intentCaptor.capture());
        assertEquals(StoreKeeperActivity.class.getName(), intentCaptor.getValue().getComponent().getClassName());
    }

    @Test
    public void testNavigateToRoleActivity_InvalidRole() {
        when(mockLoginController.isUserLoggedIn()).thenReturn(true);
        when(mockLoginController.getCurrentUser()).thenReturn(mockUserInfo);
        when(mockUserInfo.getRole()).thenReturn("UnknownRole");

        doAnswer(invocation -> {
            LoginController.OnLoginResultListener listener = invocation.getArgument(2);
            listener.onLoginSuccess(mockUserInfo);
            return null;
        }).when(mockLoginController).login(anyString(), anyString(), any());

        mainController.loginUser("unknown@example.com", "password");

        verify(mockLoginController).login(eq("unknown@example.com"), eq("password"), any());
        verify(mockLoginController, times(1)).getCurrentUser();

        verify(mockContext, never()).startActivity(any());
    }
}
