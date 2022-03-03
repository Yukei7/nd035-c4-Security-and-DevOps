package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class cartControllerTest {

	private CartController cartController;
	private final UserRepository userRepository = mock(UserRepository.class);
	private final CartRepository cartRepository = mock(CartRepository.class);
	private final ItemRepository itemRepository = mock(ItemRepository.class);

	public static Item createItem(Long id) {
		Item item = new Item();
		item.setId(id);
		item.setPrice(BigDecimal.valueOf(10));
		item.setName("Obj #" + id);
		item.setDescription("This is the description of Obj #" + id);
		return item;
	}

	public static User createUser(Long id) {
		User user = new User();
		user.setId(id);
		user.setUsername("user" + id);
		user.setPassword("userpw" + id);
		return user;
	}

	@Before
	public void setUp() {
		cartController = new CartController();
		TestUtils.injectObjects(cartController, "userRepository", userRepository);
		TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
		TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

		Item item1 = createItem(1L);
		Item item2 = createItem(2L);
		User user1 = createUser(1L);
		User user2 = createUser(2L);
		Cart cart1 = new Cart();
		Cart cart2 = new Cart();
		cart1.setUser(user1);
		cart1.addItem(item2);
		user1.setCart(cart1);

		cart2.setUser(user2);
		cart2.addItem(item1);
		cart2.addItem(item2);
		user2.setCart(cart2);

		when(userRepository.findByUsername("user1")).thenReturn(user1);
		when(userRepository.findByUsername("user2")).thenReturn(user2);
		when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
		when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
	}

	@Test
	public void addToCartSucess() {
		ModifyCartRequest req = new ModifyCartRequest();
		req.setUsername("user1");
		req.setItemId(1L);
		req.setQuantity(3);
		final ResponseEntity<Cart> response = cartController.addTocart(req);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		Cart cart = response.getBody();
		assertNotNull(cart);
		assertEquals(4, cart.getItems().size());
		assertEquals(40L, cart.getTotal().floatValue(), 1e-3);
	}

	@Test
	public void addToCartItemNotFound() {
		ModifyCartRequest req = new ModifyCartRequest();
		req.setUsername("user1");
		req.setItemId(13L);
		req.setQuantity(3);
		final ResponseEntity<Cart> response = cartController.addTocart(req);
		assertEquals(404, response.getStatusCodeValue());
	}

}
