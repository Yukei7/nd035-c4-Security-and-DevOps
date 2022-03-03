package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private OrderController orderController;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        Item item1 = TestUtils.createItem(1L);
        Item item2 = TestUtils.createItem(2L);
        User user = TestUtils.createUser(1L);
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(item1);
        cart.addItem(item2);
        user.setCart(cart);
        UserOrder userOrder = UserOrder.createFromCart(cart);

        when(userRepository.findByUsername("user1")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(userOrder));
    }

    @Test
    public void orderSubmitSuccess() {
        final ResponseEntity<UserOrder> response = orderController.submit("user1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(2, userOrder.getItems().size());
    }

    @Test
    public void getOrderByNameSuccess() {
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("user1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
        assertEquals(1, orders.size());
    }

    @Test
    public void orderSubmitNotFound() {
        final ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrderByNameNotFound() {
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
