package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private ItemController itemController;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        Item item1 = TestUtils.createItem(1L);
        Item item2 = TestUtils.createItem(2L);

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        when(itemRepository.findByName(item1.getName())).thenReturn(Arrays.asList(item1));
        when(itemRepository.findByName(item2.getName())).thenReturn(Arrays.asList(item2));
    }

    @Test
    public void getItemsSuccess() {
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void getItemsByIdSuccess() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals("item1", item.getName());
    }

    @Test
    public void getItemsByNameSuccess() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void getItemByIDNotFound() {
        ResponseEntity<Item> response = itemController.getItemById(99L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameNotFound() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
