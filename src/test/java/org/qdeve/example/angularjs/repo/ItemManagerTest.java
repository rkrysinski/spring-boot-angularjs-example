package org.qdeve.example.angularjs.repo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.qdeve.example.angularjs.data.Item.anyItem;

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
		List<Item> successList = updateResult.get(SaveStatus.SUCCESS);
		
		// Then
		assertThat(updateResult.get(SaveStatus.ERROR), equalTo(null));
		assertThat(updateResult.get(SaveStatus.NOT_ENOUGH_ITEMS), equalTo(null));
		assertThat(successList, hasSize(1));
		assertThat(successList.get(0), equalTo(uiItem));
	}
	
	@Test
	public void shouldNotBeUpdatedWhenNotEnoughItemsToBuyAndIndicateItInResponse() {
		// Given
		Item uiItem     = new Item.Builder().withCount(4).build();
		Item dbItem     = new Item.Builder().withCount(3).build();
		given(itemDAO.findOne(anyLong())).willReturn(dbItem);
		
		// When
		Map<SaveStatus, List<Item>> updateResult = itemMgr.updateItemsInDB(Arrays.asList(uiItem));
		List<Item> notEnoughItemsList = updateResult.get(SaveStatus.NOT_ENOUGH_ITEMS);
		
		// Then
		assertThat(updateResult.get(SaveStatus.ERROR), equalTo(null));
		assertThat(updateResult.get(SaveStatus.SUCCESS), equalTo(null));
		assertThat(notEnoughItemsList, hasSize(1));
		assertThat(notEnoughItemsList.get(0), equalTo(uiItem));
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
		List<Item> errorItemsList = updateResult.get(SaveStatus.ERROR);

		// Then
		assertThat(updateResult.get(SaveStatus.NOT_ENOUGH_ITEMS), equalTo(null));
		assertThat(updateResult.get(SaveStatus.SUCCESS), equalTo(null));
		assertThat(errorItemsList, hasSize(1));
		assertThat(errorItemsList.get(0), equalTo(uiItem));
	}
	
	@Test
	public void sholdFilderOutItemWithZeroCountAndRemoveItFromResponse() {
		// Given
		Item uiItem = new Item.Builder().withCount(0).build();
		
		// When
		Map<SaveStatus, List<Item>> updateResult = itemMgr.updateItemsInDB(Arrays.asList(uiItem));

		// Then
		assertThat(updateResult.get(SaveStatus.ERROR), equalTo(null));
		assertThat(updateResult.get(SaveStatus.SUCCESS), equalTo(null));
		assertThat(updateResult.get(SaveStatus.NOT_ENOUGH_ITEMS), equalTo(null));
	}

}
