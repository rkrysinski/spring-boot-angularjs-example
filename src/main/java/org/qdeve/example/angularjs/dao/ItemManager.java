package org.qdeve.example.angularjs.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.qdeve.example.angularjs.RetryConfig;
import org.qdeve.example.angularjs.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 *	Layer between REST and DB. 
 */
@Component
public class ItemManager {
	
	@Autowired
	private ItemRepository itemDAO;
	@Autowired
    private RetryConfig retryConfig;	

	public List<Item> getAll() {
		return itemDAO.findAll();
	}

	public Map<SaveStatus, List<Item>> updateItemsInDB(List<Item> items) {
		
		Map<SaveStatus, List<Item>> boughtAndNot = items.stream()
			.filter( 
				(item) -> item.getCount() > 0
			)
			.collect(Collectors.groupingBy(
				(item) -> updateItemInDB(item)
			)
		);
		return boughtAndNot;
		
	}

	private SaveStatus updateItemInDB(Item item) {
		
		SaveStatus operationStatus;
		
		try {
			operationStatus = doUpdateItemInDB(item);
		} catch (ObjectOptimisticLockingFailureException e) {
			operationStatus = SaveStatus.ERROR;
		}
		
		return operationStatus;
	}

	/**
	 * Save item in DB and retry in case of OptimisticLock exception. 
	 * @param item to save in DB
	 * @return status of save operation.
	 */
	private SaveStatus doUpdateItemInDB(Item item) {
		RetryTemplate template = retryConfig.createRetryOnOptimisticLockTemplate();
		
		return template.execute(
			(context) -> { 
				SaveStatus status = SaveStatus.NOT_ENOUGH_ITEMS;
				Item dbItem = itemDAO.findOne(item.getId());
				// do we have enough items to sell?
				if (dbItem.getCount() >= item.getCount()) {
					dbItem.buy(item.getCount());
					itemDAO.save(dbItem);
					status = SaveStatus.SUCCESS;
				}
				return status;
			}
		);
	}
}
