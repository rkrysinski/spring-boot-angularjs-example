package org.qdeve.example.angularjs.repo;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.qdeve.example.angularjs.data.ItemUtils.anyItem;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qdeve.example.angularjs.AcmeApplication;
import org.qdeve.example.angularjs.data.Item;
import org.qdeve.example.angularjs.integration.rest.ResponseMessageAssertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AcmeApplication.class)
public class ItemManagerTest {

	@Autowired
	@InjectMocks
	private ItemManager itemMgr;
	
	@Mock
	private ItemRepository itemDAO;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldBeUpdatedWhenEnoughItemsToBuyAndIndicateItInResponse() {
		// Given
		Item uiItem     = new Item.Builder().withCount(1).build();
		Item dbItem     = new Item.Builder().withCount(3).build();
		Item toSaveItem = new Item.Builder().withCount(2).build();
		given(itemDAO.findOne(anyLong())).willReturn(dbItem);
		given(itemDAO.save(eq(toSaveItem))).willReturn(anyItem());
		
		// When
		Map<SaveStatus, List<Item>> updateResult = itemMgr.updateItemsInDB(Arrays.asList(uiItem));
		
		// Then
		ResponseMessageAssertion.assertThat(updateResult)
			.hasNoItemsFor(SaveStatus.ERROR)
			.hasNoItemsFor(SaveStatus.NOT_ENOUGH_ITEMS)
			.hasExatlyGivenItemsFor(SaveStatus.SUCCESS, Arrays.asList(uiItem));
	}
	
	@Test
	public void shouldNotBeUpdatedWhenNotEnoughItemsToBuyAndIndicateItInResponse() {
		// Given
		Item uiItem     = new Item.Builder().withCount(4).build();
		Item dbItem     = new Item.Builder().withCount(3).build();
		given(itemDAO.findOne(anyLong())).willReturn(dbItem);
		
		// When
		Map<SaveStatus, List<Item>> updateResult = itemMgr.updateItemsInDB(Arrays.asList(uiItem));
		
		// Then
		ResponseMessageAssertion.assertThat(updateResult)
			.hasNoItemsFor(SaveStatus.ERROR)
			.hasNoItemsFor(SaveStatus.SUCCESS)
			.hasExatlyGivenItemsFor(SaveStatus.NOT_ENOUGH_ITEMS, Arrays.asList(uiItem));		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void shouldNotBeUpdatedWhenMultipleLockExceptionsAndIndicateItInResponse() {
		// Given
		Item uiItem     = new Item.Builder().withCount(1).build();
		Item dbItem     = new Item.Builder().withCount(3).build();
		given(itemDAO.findOne(anyLong())).willReturn(dbItem);
		given(itemDAO.save(any(Item.class)))
			.willThrow(ObjectOptimisticLockingFailureException.class);
		
		// When
		Map<SaveStatus, List<Item>> updateResult = itemMgr.updateItemsInDB(Arrays.asList(uiItem));

		// Then
		ResponseMessageAssertion.assertThat(updateResult)
			.hasNoItemsFor(SaveStatus.NOT_ENOUGH_ITEMS)
			.hasNoItemsFor(SaveStatus.SUCCESS)
			.hasExatlyGivenItemsFor(SaveStatus.ERROR, Arrays.asList(uiItem));		
	}
	
	@Test
	public void sholdFilderOutItemWithZeroCountAndRemoveItFromResponse() {
		// Given
		Item uiItem = new Item.Builder().withCount(0).build();
		
		// When
		Map<SaveStatus, List<Item>> updateResult = itemMgr.updateItemsInDB(Arrays.asList(uiItem));

		// Then
		ResponseMessageAssertion.assertThat(updateResult)
			.hasNoItemsFor(SaveStatus.ERROR)
			.hasNoItemsFor(SaveStatus.SUCCESS)
			.hasNoItemsFor(SaveStatus.NOT_ENOUGH_ITEMS);
	}

}
