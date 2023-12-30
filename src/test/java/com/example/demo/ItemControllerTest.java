package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import java.util.Optional;

import static org.junit.Assert.*;

public class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;
    @Mock
    private final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    private static Item createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("itemName");
        return item;
    }

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getItems_200(){
        Mockito.when(itemRepository.findAll())
                .thenReturn(new ArrayList<>());
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getItems_200_2(){
        Item item = createItem();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Mockito.when(itemRepository.findAll())
                .thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(response.getBody().size(), 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getItemById_200(){
        Item item = createItem();
        Mockito.when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getItemById_404(){
        Mockito.when(itemRepository.findById(1L))
                .thenReturn(Optional.empty());
        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getItemsByName_404_null(){
        Mockito.when(itemRepository.findByName("itemName"))
                .thenReturn(null);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("itemName");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getItemsByName_404_emptyList(){
        Mockito.when(itemRepository.findByName("itemName"))
                .thenReturn(new ArrayList<>());
        ResponseEntity<List<Item>> response = itemController.getItemsByName("itemName");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getItemsByName_200(){
        Item item = createItem();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Mockito.when(itemRepository.findByName("itemName"))
                .thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("itemName");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
