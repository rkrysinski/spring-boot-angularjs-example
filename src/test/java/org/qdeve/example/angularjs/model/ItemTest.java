package org.qdeve.example.angularjs.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.qdeve.example.angularjs.model.Item;

public class ItemTest {

	@Test
	public void shouldBeTheSameIfCountAndNameEquals() {
		// given
		Item itemA = new Item.Builder().withCount(3).withName("SomeName").build();
		Item itemB = new Item.Builder().withCount(3).withName("SomeName").build();

		// then
		assertThat(itemA.equals(itemB), equalTo(true));
		assertThat(itemB.equals(itemA), equalTo(true));
	}
	
	@Test
	public void shouldNotBeTheSameIfOneNameIsNull() {
		// given
		Item itemA = new Item.Builder().withCount(3).withName(null).build();
		Item itemB = new Item.Builder().withCount(3).withName("SomeName").build();
		
		// then
		assertThat(itemA.equals(itemB), equalTo(false));
		assertThat(itemB.equals(itemA), equalTo(false));
	}
}
