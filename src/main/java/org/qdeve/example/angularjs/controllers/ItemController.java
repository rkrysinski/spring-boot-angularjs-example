package org.qdeve.example.angularjs.controllers;

import java.util.ArrayList;
import java.util.List;

import org.qdeve.example.angularjs.data.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ItemController.REQUEST_MAPPING)
public class ItemController {

	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);
	public static final String REQUEST_MAPPING = "/item";

	@RequestMapping(method = RequestMethod.GET)
	ResponseEntity<List<Item>> getAll() {
		LOG.debug("getAll entered");

		List<Item> items = new ArrayList<>();
		items.add(new Item.Builder()
					.withItemName("Item A")
					.withCount(20)
					.build()
		);
		items.add(new Item.Builder()
			.withItemName("Item B")
			.withCount(10)
			.build()
		);

		return new ResponseEntity<>(items, HttpStatus.OK);
	}
}
