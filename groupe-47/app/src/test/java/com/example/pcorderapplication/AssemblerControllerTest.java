package com.example.pcorderapplication;

import android.content.Context;

import com.example.pcorderapplication.controller.AssemblerController;
import com.example.pcorderapplication.model.database.AccessLocal;
import com.example.pcorderapplication.model.database.OrderRepository;
import com.example.pcorderapplication.model.database.UserRepository;
import com.example.pcorderapplication.model.orders.Order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class AssemblerControllerTest {

    @Mock
    Context mockContext;

    @Mock
    AccessLocal mockAccessLocal;

    @Mock
    OrderRepository mockOrderRepository;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    Order mockOrder;

    private AssemblerController assemblerController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);


        assemblerController = new AssemblerController(mockOrderRepository, mockUserRepository, mockAccessLocal);

        when(mockOrder.getId()).thenReturn(1);
    }

    @Test
    public void testAddOrder_Success() {
        when(mockOrderRepository.findOrderById(anyInt(), eq(mockUserRepository))).thenReturn(null);

        boolean result = assemblerController.addOrder(mockOrder);

        assertTrue(result);
        verify(mockOrderRepository, times(1)).insertOrder(mockOrder);
    }

    @Test
    public void testAddOrder_Failure_OrderExists() {
        when(mockOrderRepository.findOrderById(anyInt(), eq(mockUserRepository))).thenReturn(mockOrder);

        boolean result = assemblerController.addOrder(mockOrder);

        assertFalse(result);
        verify(mockOrderRepository, never()).insertOrder(any(Order.class));
    }

    @Test
    public void testViewAllOrders() {
        List<Order> mockOrders = Arrays.asList(mockOrder);
        when(mockOrderRepository.getAllOrders(eq(mockUserRepository))).thenReturn(mockOrders);

        List<Order> orders = assemblerController.viewAllOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(mockOrderRepository, times(1)).getAllOrders(eq(mockUserRepository));
    }

    @Test
    public void testAcceptOrder() {
        when(mockOrderRepository.updateOrder(mockOrder)).thenReturn(true);

        assemblerController.acceptOrder(mockOrder);

        verify(mockOrder, times(1)).updateStatus("Accepted");
        verify(mockOrderRepository, times(1)).updateOrder(mockOrder);
    }


    @Test
    public void testRejectOrder() {
        when(mockOrderRepository.updateOrder(mockOrder)).thenReturn(true);

        boolean result = assemblerController.rejectOrder(mockOrder, "Invalid order");

        assertTrue(result);
        verify(mockOrder, times(1)).updateStatus("Rejected");
        verify(mockOrder, times(1)).setMessage("Invalid order");
        verify(mockOrderRepository, times(1)).updateOrder(mockOrder);
    }


    @Test
    public void testRejectOrder_NullOrder() {
        boolean result = assemblerController.rejectOrder(null, "Invalid order");

        assertFalse(result);
        verify(mockOrderRepository, never()).updateOrder(any(Order.class));
    }

    @Test
    public void testCompleteOrder() {
        when(mockOrderRepository.updateOrder(mockOrder)).thenReturn(true);

        assemblerController.completeOrder(mockOrder);

        verify(mockOrder, times(1)).updateStatus("Completed");
        verify(mockOrderRepository, times(1)).updateOrder(mockOrder);
    }


    @Test
    public void testFindOrderById_Success() {
        when(mockOrderRepository.findOrderById(anyInt(), eq(mockUserRepository))).thenReturn(mockOrder);

        Order order = assemblerController.findOrderById(1);

        assertNotNull(order);
        verify(mockOrderRepository, times(1)).findOrderById(1, mockUserRepository);
    }

    @Test
    public void testFindOrderById_Failure() {
        when(mockOrderRepository.findOrderById(anyInt(), eq(mockUserRepository))).thenReturn(null);

        Order order = assemblerController.findOrderById(1);

        assertNull(order);
        verify(mockOrderRepository, times(1)).findOrderById(1, mockUserRepository);
    }
}