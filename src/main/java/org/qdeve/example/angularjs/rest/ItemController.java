package org.qdeve.example.angularjs.rest;

import java.util.List;
import java.util.Map;

import org.qdeve.example.angularjs.data.Item;
import org.qdeve.example.angularjs.repo.ItemManager;
import org.qdeve.example.angularjs.repo.SaveStatus;
import org.springframework.beans.factory.annotation.Autowired;
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

	/**
	 * Retrieves items from UI and pushes it to DB.
	 * 
	 * NOTE: Item.count coming from UI contains the number of items to buy.
	 * 
	 * @param items coming from UI
	 * @return the ResponseMessage.
	 */
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
	ResponseEntity<ResponseMessage> updateItems(@RequestBody List<Item> items ) {
		
		Map<SaveStatus, List<Item>> updateResult = itemMgr.updateItemsInDB(items);
		
		return ResponseEntity.ok(ResponseMessage.fromResult(updateResult));
	}

	@RequestMapping(method = RequestMethod.GET)
	ResponseEntity<List<Item>> getAllItems() {

		List<Item> items = itemMgr.getAll();
		
		return ResponseEntity.ok(items);
	}
}
