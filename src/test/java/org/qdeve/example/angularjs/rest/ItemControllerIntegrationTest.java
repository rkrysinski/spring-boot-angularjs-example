package org.qdeve.example.angularjs.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qdeve.example.angularjs.AcmeApplication;
import org.qdeve.example.angularjs.data.Item;
import org.qdeve.example.angularjs.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AcmeApplication.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class ItemControllerIntegrationTest {

	@Value("${local.server.port}")
	private int port;

	@Autowired
	private ItemRepository itemDAO;
	
	private URI base;
	private RestTemplate template;
	private List<Item> dbItems;
	
	@Before
	public void setUp() throws Exception {
		base     = new URI("http://localhost:" + port + ItemController.REQUEST_MAPPING);
		template = new TestRestTemplate();
		dbItems  = new ArrayList<>();
		dbItems.add(new Item.Builder().withName("Item A").withCount(20).build());
		dbItems.add(new Item.Builder().withName("Item B").withCount(10).build());
		dbItems.add(new Item.Builder().withName("Item C").withCount(50).build());		
	}
	
	@Test
	public void shouldReturnAllItemsFromDBWhenAccessingItemURL() {
		// given
		theDefaultItemsInDB();
		
		// when
		Item[] retrievedItems = template.getForObject(base, Item[].class);
		
		// then
		assertThat(dbItems, contains(retrievedItems));
	}
	
	@Test
	public void shouldReturnSuccessWhenItemsAreInStock() {
		fail("Not implemented yet");
	}
	
	@Test
	public void shouldReturnNotEoughItemsStatusWhenNotEnoughItemsInShop() {
		fail("Not implemented yet");
	}
	 
	@Test
	public void shouldReturnErrorInCaseOfLockingException() {
		fail("Not implemented yet");
	}
	
	
	private void theDefaultItemsInDB() {
		itemDAO.deleteAll();
		itemDAO.save(dbItems);
	}

}
