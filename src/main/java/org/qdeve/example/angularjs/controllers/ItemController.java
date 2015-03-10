package org.qdeve.example.angularjs.controllers;

import java.util.ArrayList;
import java.util.List;

import org.qdeve.example.angularjs.data.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ItemController.REQUEST_MAPPING)
public class ItemController {

	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);
	public static final String REQUEST_MAPPING = "/item";

	@RequestMapping(method = RequestMethod.PUT)
	ResponseEntity<Void> update(@RequestBody List<Item> items) {
		LOG.debug("\n\n\n\n\n\nReceived: " + items);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	ResponseEntity<List<Item>> getAll() {
		LOG.debug("getAll entered");

		List<Item> items = new ArrayList<>();
		items.add(new Item.Builder().withName("Item A").withCount(20).build());
		items.add(new Item.Builder().withName("Item B").withCount(10).build());
		items.add(new Item.Builder().withName("Item C").withCount(50).build());

		return new ResponseEntity<>(items, HttpStatus.OK);
	}
}
