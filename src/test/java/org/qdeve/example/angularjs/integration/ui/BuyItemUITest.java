package org.qdeve.example.angularjs.integration.ui;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.qdeve.example.angularjs.data.Item;
import org.qdeve.example.angularjs.repo.ItemManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Sanity UI testing.
 */
public class BuyItemUITest extends SeleniumTestBase {

	@Autowired
	private ItemManager itemMgr;
	
	@Test
	public void shouldBuyItemsWhenInStock()
	{
		// given
		driver.get(baseURL.toString());
		String itemCountInShop = driver.findElement(By.id("shop_item_count_0")).getText();
		driver.findElement(By.id("tobuy_input_count_0")).sendKeys("1");
		
		//when
		driver.findElement(By.id("buy")).click();
		wait.until(ExpectedConditions
				.textToBePresentInElementLocated(
						By.id("shop_item_count_0"), "19"));
		String itemCountInShopAfterBuy = driver.findElement(By.id("shop_item_count_0")).getText();
		String boughtItemName  = driver.findElement(By.id("success_result_item_name_0")).getText();
		String boughtItemCount = driver.findElement(By.id("success_result_count_0")).getText();
		
		// then
		assertThat(itemCountInShop, equalTo(Integer.toString(Item.Builder.DEFAULT_COUNT_20)));
		assertThat(itemCountInShopAfterBuy, equalTo("19"));
		assertThat(boughtItemName,  equalTo(Item.Builder.DEFAULT_NAME));
		assertThat(boughtItemCount, equalTo("1"));
	}
	
	@Test
	public void shouldNotBuyItemsWhenOutOfStock()
	{
		// given
		driver.get(baseURL.toString());
		driver.findElement(By.id("tobuy_input_count_0")).sendKeys("1");

		//when: update in between before clicking buy button
		Item toBuy = new Item.Builder().fromItem(dbItem).withCount(Item.Builder.DEFAULT_COUNT_20).build();
		itemMgr.updateItemsInDB(Arrays.asList(toBuy));

		driver.findElement(By.id("buy")).click();
		wait.until(ExpectedConditions
				.presenceOfElementLocated(
						By.id("not_enough_result_count_0")));
		String failedItemName  = driver.findElement(By.id("not_enough_result_item_name_0")).getText();
		String failedItemCount = driver.findElement(By.id("not_enough_result_count_0")).getText();

		// then
		assertThat(failedItemName,  equalTo(Item.Builder.DEFAULT_NAME));
		assertThat(failedItemCount, equalTo("1"));
	}
	
}
