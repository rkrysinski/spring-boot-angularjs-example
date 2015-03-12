package org.qdeve.example.angularjs.controllers;

import java.util.List;

import org.qdeve.example.angularjs.data.Item;
import org.qdeve.example.angularjs.repo.ItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ItemController.REQUEST_MAPPING)
public class ItemController {

	public static final String REQUEST_MAPPING = "/item";
	
	@Autowired
	private ItemManager itemMgr;

	@RequestMapping(method = RequestMethod.PUT)
	ResponseEntity<List<Item>> update(@RequestBody List<Item> items) {
		
		//TODO: change return value to have string with status of order
		//      make the message coming from properties file (EN).
		List<Item> updatedItems = itemMgr.update(items);
		
		return new ResponseEntity<>(updatedItems, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	ResponseEntity<List<Item>> getAll() {

		List<Item> items = itemMgr.getAll();
		
		return new ResponseEntity<>(items, HttpStatus.OK);
	}
}
