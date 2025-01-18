package com.example.pcorderapplication;

import android.content.Context;

import com.example.pcorderapplication.controller.RequesterController;
import com.example.pcorderapplication.model.database.AccessLocal;
import com.example.pcorderapplication.model.entity.Component;
import com.example.pcorderapplication.model.orders.Order;
import com.example.pcorderapplication.model.users.Requester;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28}, manifest = Config.NONE)
public class RequesterControllerTest {

    @Mock
    Context mockContext;

    @Mock
    AccessLocal mockAccessLocal;

    @Mock
    Requester mockRequester;

    @Mock
    Component mockComponent;

    @Mock
    Order mockOrder;

    private RequesterController requesterController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        requesterController = RequesterController.getInstance(mockContext, "John", "Doe", "john.doe@example.com", "password123");

        Field requesterField = RequesterController.class.getDeclaredField("requester");
        requesterField.setAccessible(true);
        requesterField.set(null, mockRequester);

        Field accessLocalField = RequesterController.class.getDeclaredField("accessLocal");
        accessLocalField.setAccessible(true);
        accessLocalField.set(null, mockAccessLocal);
    }

    @Test
    public void testCreateOrder_Success() {
        when(mockAccessLocal.findComponentByTitle(anyString())).thenReturn(mockComponent);
        when(mockComponent.getQuantity()).thenReturn(10);
        when(mockAccessLocal.updateComponent(any(Component.class))).thenReturn(1L);

        List<Component> components = new ArrayList<>();
        components.add(new Component("SampleTitle", "TypeA", "SubtypeB", 5, "Test comment", "2023-11-22", "2023-11-23"));

        boolean result = requesterController.createOrder(components);

        assertTrue(result);
        verify(mockAccessLocal, times(1)).updateComponent(any(Component.class));
    }

    @Test
    public void testCreateOrder_Failure_NoComponentsAvailable() {
        when(mockAccessLocal.findComponentByTitle("SampleTitle")).thenReturn(null);

        List<Component> components = new ArrayList<>();
        components.add(new Component("SampleTitle", "TypeA", "SubtypeB", 5, "Test comment", "2023-11-22", "2023-11-23"));

        boolean result = requesterController.createOrder(components);

        assertFalse(result);
        verify(mockAccessLocal, times(1)).findComponentByTitle("SampleTitle");
        verify(mockAccessLocal, never()).updateComponent(any(Component.class));
        verifyNoMoreInteractions(mockAccessLocal);
    }

    @Test
    public void testDeleteOrder() {
        doNothing().when(mockRequester).deleteOrder(anyInt());

        requesterController.deleteOrder(1);

        verify(mockRequester, times(1)).deleteOrder(1);
    }

    @Test
    public void testViewOwnOrders() {
        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(mockOrder);
        when(mockRequester.viewOwnOrders()).thenReturn(mockOrders);

        List<Order> orders = requesterController.viewOwnOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(mockRequester, times(1)).viewOwnOrders();
    }

    @Test
    public void testValidateComponents_Success() {
        when(mockAccessLocal.findComponentByTitle(anyString())).thenReturn(mockComponent);
        when(mockComponent.getQuantity()).thenReturn(10);
        when(mockAccessLocal.updateComponent(any(Component.class))).thenReturn(1L);

        Component component = new Component("SampleTitle", "TypeA", "SubtypeB", 5, "Comment here", "2023-11-22", "2023-11-23");

        boolean result = requesterController.validateComponents(component);

        assertTrue(result);
        verify(mockAccessLocal, times(1)).updateComponent(any(Component.class));
    }

    @Test
    public void testValidateComponents_Failure() {
        when(mockAccessLocal.findComponentByTitle(anyString())).thenReturn(mockComponent);
        when(mockComponent.getQuantity()).thenReturn(2);

        Component component = new Component("SampleTitle", "TypeA", "SubtypeB", 5, "Comment here", "2023-11-22", "2023-11-23");

        boolean result = requesterController.validateComponents(component);

        assertFalse(result);
        verify(mockAccessLocal, never()).updateComponent(any(Component.class));
    }
}
