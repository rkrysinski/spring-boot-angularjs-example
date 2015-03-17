package org.qdeve.example.angularjs.integration.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qdeve.example.angularjs.AcmeApplication;
import org.qdeve.example.angularjs.controller.ItemController;
import org.qdeve.example.angularjs.controller.ResponseMessage;
import org.qdeve.example.angularjs.dao.ItemRepository;
import org.qdeve.example.angularjs.dao.SaveStatus;
import org.qdeve.example.angularjs.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
	private Item dbItemUnderTest;
	
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
	public void shouldReturnAllItemsFromDBWhenAccessingItemsURL() {
		// given
		theDefaultItemsInDB();
		
		// when
		Item[] retrievedItems = template.getForObject(base, Item[].class);
		
		// then
		assertThat(dbItems, contains(retrievedItems));
	}
	
	@Test
	public void shouldReturnSuccessWhenItemsAreInStockAndDecreaseAvailableCount() {
		// given
		theDefaultItemsInDB();
		Item uiItem = new Item.Builder().fromItem(dbItemUnderTest).withCount(1).build();
		
		// when
		ResponseEntity<ResponseMessage> response = template.exchange(
				base, 
				HttpMethod.PUT, 
				new HttpEntity<List<Item>>(Arrays.asList(uiItem)), 
				ResponseMessage.class);
		
		// then
		ResponseMessageAssertion.assertThat(response.getBody())
			.responseAndContentNotNull()
			.hasResponseItemsFor(SaveStatus.SUCCESS)
			.hasNoItemsFor(SaveStatus.ERROR)
			.hasNoItemsFor(SaveStatus.NOT_ENOUGH_ITEMS);
		Item dbItem = itemDAO.findOne(dbItemUnderTest.getId());
		assertThat(dbItem.getCount(), equalTo(dbItemUnderTest.getCount() - 1));
	}
	
	@Test
	public void shouldNotBuyItemsWhenNotAvailableInShopAndReturnNotEoughStatus() {
		// given
		theDefaultItemsInDB();
		Item uiItem = new Item.Builder().fromItem(dbItemUnderTest).withCount(Integer.MAX_VALUE).build();
		
		// when
		ResponseEntity<ResponseMessage> response = template.exchange(
				base, 
				HttpMethod.PUT, 
				new HttpEntity<List<Item>>(Arrays.asList(uiItem)), 
				ResponseMessage.class);
		
		// then
		ResponseMessageAssertion.assertThat(response.getBody())
			.responseAndContentNotNull()
			.hasResponseItemsFor(SaveStatus.NOT_ENOUGH_ITEMS)
			.hasNoItemsFor(SaveStatus.ERROR)
			.hasNoItemsFor(SaveStatus.SUCCESS);
		Item dbItem = itemDAO.findOne(dbItemUnderTest.getId());
		assertThat(dbItem.getCount(), equalTo(dbItemUnderTest.getCount()));
	}
	 
	@Test
	public void shouldBuyItemsIfAvailableEvenUpdateInBetween() {
		// given
		theDefaultItemsInDB();
		Item uiItem = new Item.Builder().fromItem(dbItemUnderTest).withCount(1).build();
		
		// when: two updates, the second update results with OptimisticLock exception
		template.exchange(
				base, 
				HttpMethod.PUT, 
				new HttpEntity<List<Item>>(Arrays.asList(uiItem)), 
				ResponseMessage.class);
		ResponseEntity<ResponseMessage> response = template.exchange(
				base, 
				HttpMethod.PUT, 
				new HttpEntity<List<Item>>(Arrays.asList(uiItem)), 
				ResponseMessage.class);
		
		// then: the second response eventually is a success
		ResponseMessageAssertion.assertThat(response.getBody())
			.responseAndContentNotNull()
			.hasResponseItemsFor(SaveStatus.SUCCESS)
			.hasNoItemsFor(SaveStatus.ERROR)
			.hasNoItemsFor(SaveStatus.NOT_ENOUGH_ITEMS);
		Item dbItem = itemDAO.findOne(dbItemUnderTest.getId());
		assertThat(dbItem.getCount(), equalTo(dbItemUnderTest.getCount() - 2));
	}
	
	
	private void theDefaultItemsInDB() {
		itemDAO.deleteAll();
		dbItems = itemDAO.save(dbItems);
		dbItemUnderTest = dbItems.get(1);
	}

}
