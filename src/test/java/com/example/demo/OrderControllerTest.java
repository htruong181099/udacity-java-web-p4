package com.example.demo;


import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;
    @Mock
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Mock
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    public static User createUser(String username){
        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        Item item1 = createItem(1L);
        Item item2 = createItem(2L);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item1);
        itemList.add(item2);

        Cart cart = new Cart();
        cart.setItems(itemList);

        user.setCart(cart);
        return user;
    }
    private static Item createItem(Long id){
        Item item = new Item();
        item.setId(id);
        item.setName("itemName" + id);
        return item;
    }

    @Test
    public void submit_404(){
        String username = "username";
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit(username);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void submit_200(){
        String username = "username";
        User user = createUser(username);

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(username);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void getOrdersForUser_404(){
        String username = "username";
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void getOrdersForUser_200(){
        String username = "username";
        User user = createUser(username);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(user);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

}
