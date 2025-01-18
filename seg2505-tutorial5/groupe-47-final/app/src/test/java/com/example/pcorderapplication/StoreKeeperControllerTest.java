package com.example.pcorderapplication;

import android.content.Context;
import android.util.Log;

import com.example.pcorderapplication.controller.StoreKeeperController;
import com.example.pcorderapplication.model.database.AccessLocal;
import com.example.pcorderapplication.model.entity.Component;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StoreKeeperControllerTest {

    @Mock
    Context mockContext;

    @Mock
    AccessLocal mockAccessLocal;

    private StoreKeeperController storeKeeperController;
    private MockedStatic<Log> mockedLog;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedLog = mockStatic(Log.class);
        mockedLog.when(() -> Log.i(anyString(), anyString())).thenReturn(0);
        storeKeeperController = StoreKeeperController.getInstance(mockContext);
        injectAccessLocalMock(mockAccessLocal);
    }

    @After
    public void tearDown() {
        mockedLog.close();
    }

    private void injectAccessLocalMock(AccessLocal mockAccessLocal) {
        try {
            Field accessLocalField = StoreKeeperController.class.getDeclaredField("accessLocal");
            accessLocalField.setAccessible(true);
            accessLocalField.set(null, mockAccessLocal);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject AccessLocal mock", e);
        }
    }

    @Test
    public void testAddComponent_Success() {
        String title = "TestComponent";
        String type = "TypeA";
        String subtype = "SubtypeB";
        int quantity = 10;
        String comment = "Test comment";
        int requestCount = 5;
        String modificationDate = "2023-11-23";

        when(mockAccessLocal.findComponentByTitle(title)).thenReturn(null);
        when(mockAccessLocal.addComponent(any(Component.class))).thenReturn(1L);

        boolean result = storeKeeperController.addComponent(title, type, subtype, quantity, comment, requestCount, modificationDate);

        assertTrue(result);
        verify(mockAccessLocal, times(1)).findComponentByTitle(title);
        verify(mockAccessLocal, times(1)).addComponent(any(Component.class));
    }

    @Test
    public void testAddComponent_Failure_TitleExists() {
        String title = "TestComponent";

        when(mockAccessLocal.findComponentByTitle(title)).thenReturn(mock(Component.class));

        boolean result = storeKeeperController.addComponent(title, "TypeA", "SubtypeB", 10, "Test comment", 5, "2023-11-23");

        assertFalse(result);
        verify(mockAccessLocal, times(1)).findComponentByTitle(title);
        verify(mockAccessLocal, never()).addComponent(any(Component.class));
    }

    @Test
    public void testFindComponentByTitle() {
        String title = "TestComponent";
        Component mockComponent = mock(Component.class);

        when(mockAccessLocal.findComponentByTitle(title)).thenReturn(mockComponent);

        Component result = storeKeeperController.findComponentByTitle(title);

        assertNotNull(result);
        assertEquals(mockComponent, result);
        verify(mockAccessLocal, times(1)).findComponentByTitle(title);
    }

    @Test
    public void testUpload() {
        ArrayList<String> mockUploadData = new ArrayList<>();
        mockUploadData.add("Component1");
        mockUploadData.add("Component2");

        when(mockAccessLocal.upload()).thenReturn(mockUploadData);

        List<String> result = storeKeeperController.upload();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Component1", result.get(0));
        assertEquals("Component2", result.get(1));
        verify(mockAccessLocal, times(1)).upload();
    }

    @Test
    public void testUpdateComponent_Success() {
        String title = "TestComponent";
        String type = "TypeA";
        String subtype = "SubtypeB";
        int quantity = 20;
        String comment = "Updated comment";
        int requestCount = 8;
        String modificationDate = "2023-11-24";

        when(mockAccessLocal.updateComponent(any(Component.class))).thenReturn(1L);

        boolean result = storeKeeperController.updateComponent(title, type, subtype, quantity, comment, requestCount, modificationDate);

        assertTrue(result);
        verify(mockAccessLocal, times(1)).updateComponent(any(Component.class));
    }

    @Test
    public void testUpdateComponent_Failure() {
        String title = "TestComponent";

        when(mockAccessLocal.updateComponent(any(Component.class))).thenReturn(-1L);

        boolean result = storeKeeperController.updateComponent(title, "TypeA", "SubtypeB", 20, "Updated comment", 8, "2023-11-24");

        assertFalse(result);
        verify(mockAccessLocal, times(1)).updateComponent(any(Component.class));
    }

    @Test
    public void testDeleteComponent_Success() {
        String title = "TestComponent";

        when(mockAccessLocal.deleteComponent(title)).thenReturn(1);

        boolean result = storeKeeperController.deleteComponent(title);

        assertTrue(result);
        verify(mockAccessLocal, times(1)).deleteComponent(title);
    }

    @Test
    public void testDeleteComponent_Failure() {
        String title = "TestComponent";

        when(mockAccessLocal.deleteComponent(title)).thenReturn(-1);

        boolean result = storeKeeperController.deleteComponent(title);

        assertFalse(result);
        verify(mockAccessLocal, times(1)).deleteComponent(title);
    }
}
