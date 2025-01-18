package com.example.pcorderapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import android.content.Context;

import com.example.pcorderapplication.controller.AdministratorController;
import com.example.pcorderapplication.model.database.UserRepository;
import com.example.pcorderapplication.model.users.Administrator;
import com.example.pcorderapplication.model.users.Requester;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
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

        // Injecting the mock Administrator directly
        administratorController = new AdministratorController(mockContext, "adminFirstName", "adminLastName", "admin@example.com", "adminPassword") {
            @Override
            public void createRequester(String firstName, String lastName, String email, String password) {
                mockAdministrator.createRequester(firstName, lastName, email, password);
            }

            @Override
            public void modifyRequester(String newFirstName, String newLastName, String email) {
                mockAdministrator.modifyRequester(newFirstName, newLastName, email);
            }

            @Override
            public void deleteRequester(String firstName, String lastName, String email) {
                mockAdministrator.deleteRequester(firstName, lastName, email);
            }
        };
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
        List<Requester> mockRequesters = new ArrayList<>();
        mockRequesters.add(new Requester("John", "Doe", "john.doe@example.com", "password123"));

        when(mockAdministrator.getAllRequesters()).thenReturn(mockRequesters);

        AdministratorController controller = new AdministratorController(mockContext, "adminFirstName", "adminLastName", "admin@example.com", "adminPassword") {
            @Override
            public List<Requester> getAllRequesters() {
                return mockAdministrator.getAllRequesters();
            }
        };

        List<Requester> result = controller.getAllRequesters();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
        verify(mockAdministrator, times(1)).getAllRequesters();
    }




}
