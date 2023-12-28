package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CartControllerTest {
    @InjectMocks
    private CartController cartController;
    @Mock
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Mock
    private final CartRepository cartRepository = Mockito.mock(CartRepository.class);
    @Mock
    private final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    public static User createUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setCart(new Cart());
        return user;
    }

    public static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Chair");
        item.setDescription("To sit");
        item.setPrice(BigDecimal.TEN);
        return item;
    }

    public static ModifyCartRequest createModifyCartRequest(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("username");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(4);
        return modifyCartRequest;
    }

    @Test
    public void addTocart_404_UserNotFound() {
        ResponseEntity<Cart> response = cartController.addTocart(createModifyCartRequest());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addTocart_404_ItemNotFound() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        User user = createUser();
        Mockito.when(userRepository.findByUsername(modifyCartRequest.getUsername()))
                .thenReturn(user);
        ResponseEntity<Cart> response = cartController.addTocart(createModifyCartRequest());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addTocart_200() {
        User user = createUser();
        Item item = createItem();
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        Mockito.when(userRepository.findByUsername(modifyCartRequest.getUsername()))
                .thenReturn(user);
        Mockito.when(itemRepository.findById(modifyCartRequest.getItemId()))
                .thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.addTocart(createModifyCartRequest());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(modifyCartRequest.getQuantity(), cart.getItems().size());
    }

    @Test
    public void removeFromcart_404_UserNotFound() {
        ResponseEntity<Cart> response = cartController.removeFromcart(createModifyCartRequest());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void removeFromcart_404_ItemNotFound() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        User user = createUser();
        Mockito.when(userRepository.findByUsername(modifyCartRequest.getUsername()))
                .thenReturn(user);
        ResponseEntity<Cart> response = cartController.removeFromcart(createModifyCartRequest());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void removeFromcart_200() {
        User user = createUser();
        Item item = createItem();
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        Mockito.when(userRepository.findByUsername(modifyCartRequest.getUsername()))
                .thenReturn(user);
        Mockito.when(itemRepository.findById(modifyCartRequest.getItemId()))
                .thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.removeFromcart(createModifyCartRequest());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
    }
}
