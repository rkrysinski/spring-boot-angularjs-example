package org.qdeve.example.angularjs.integration.ui;

import static org.openqa.selenium.By.id;

import java.util.Arrays;

import org.junit.Test;
import org.qdeve.example.angularjs.dao.ItemManager;
import org.qdeve.example.angularjs.model.Item;
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
		fwd.input(id("tobuy_input_count_0")).sendKeys("1");
		
		//when
		fwd.input(id("buy")).click();
		fwd.span(ngWait(id("shop_item_count_0")));
		
		// then
		fwd.span(id("shop_item_count_0")).getText().shouldBe("19");
		fwd.span(id("success_result_item_name_0")).getText().shouldBe(Item.Builder.DEFAULT_NAME);
		fwd.span(id("success_result_count_0")).getText().shouldBe("1");
		
	}
	
	@Test
	public void shouldNotBuyItemsWhenOutOfStock()
	{
		// given
		fwd.input(id("tobuy_input_count_0")).sendKeys("1");

		//when: update in between before clicking buy button
		Item toBuy = new Item.Builder().fromItem(dbItem).withCount(Item.Builder.DEFAULT_COUNT_20).build();
		itemMgr.updateItemsInDB(Arrays.asList(toBuy));

		fwd.input(id("buy")).click();

		// then
		fwd.span(id("not_enough_result_item_name_0")).getText().shouldBe(Item.Builder.DEFAULT_NAME);
		fwd.span(id("not_enough_result_count_0")).getText().shouldBe("1");
	}
	
}
