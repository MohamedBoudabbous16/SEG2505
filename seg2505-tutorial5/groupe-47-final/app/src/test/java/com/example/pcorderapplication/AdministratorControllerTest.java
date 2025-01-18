package com.example.pcorderapplication;

import static org.mockito.Mockito.*;

import android.content.Context;

import com.example.pcorderapplication.controller.AdministratorController;
import com.example.pcorderapplication.model.users.Administrator;
import com.example.pcorderapplication.model.users.Requester;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class AdministratorControllerTest {

    @Mock
    private Context mockContext;

    @Mock
    private Administrator mockAdministrator;

    private AdministratorController administratorController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize AdministratorController using expected parameters
        administratorController = AdministratorController.getInstance(
                mockContext,
                "AdminFirstName",
                "AdminLastName",
                "admin@example.com",
                "password123"
        );

        // Use reflection or a setter to replace the internal Administrator with a mock
        replaceAdministratorWithMock();
    }

    private void replaceAdministratorWithMock() {
        try {
            java.lang.reflect.Field administratorField = AdministratorController.class.getDeclaredField("administrator");
            administratorField.setAccessible(true);
            administratorField.set(administratorController, mockAdministrator);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock Administrator", e);
        }
    }

    @Test
    public void testCreateRequester() {
        doNothing().when(mockAdministrator).createRequester(anyString(), anyString(), anyString(), anyString());

        administratorController.createRequester("John", "Doe", "john.doe@example.com", "password123");

        verify(mockAdministrator, times(1)).createRequester("John", "Doe", "john.doe@example.com", "password123");
    }

    @Test
    public void testModifyRequester() {
        doNothing().when(mockAdministrator).modifyRequester(anyString(), anyString(), anyString());

        administratorController.modifyRequester("Jane", "Doe", "jane.doe@example.com");

        verify(mockAdministrator, times(1)).modifyRequester("Jane", "Doe", "jane.doe@example.com");
    }

    @Test
    public void testDeleteRequester() {
        doNothing().when(mockAdministrator).deleteRequester(anyString(), anyString(), anyString());

        administratorController.deleteRequester("Jake", "Doe", "jake.doe@example.com");

        verify(mockAdministrator, times(1)).deleteRequester("Jake", "Doe", "jake.doe@example.com");
    }

    @Test
    public void testGetAllRequesters() {
        when(mockAdministrator.getAllRequesters()).thenReturn(Collections.emptyList());

        List<Requester> requesters = administratorController.getAllRequesters();

        verify(mockAdministrator, times(1)).getAllRequesters();
        assert requesters.isEmpty();
    }
}
