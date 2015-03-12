package org.qdeve.example.angularjs.repo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.qdeve.example.angularjs.RetryConfig;
import org.qdeve.example.angularjs.data.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class ItemManager {

	@Autowired
	private ItemRepository itemDAO;
	@Autowired
    private RetryConfig retryConfig;	

	public List<Item> getAll() {
		return itemDAO.findAll();
	}

	public List<Item> update(List<Item> items) {
		
		Map<Boolean, List<Item>> boughtAndNot = items.parallelStream().collect(
			Collectors.partitioningBy(
				(item) -> saveToDB(item)
			)
		);
		List<Item> notUpdated = boughtAndNot.get(Boolean.FALSE);
		if (!notUpdated.isEmpty()) {
			throw new DataIntegrityViolationException(
				String.format("Failed to buy the following item(s): \"%s\". " +
						"It's very likely that we are out for the particular item(s). Please try again.", 
						 notUpdated.stream()
						 	.map(Item::getName)
						    .collect(Collectors.joining(",")))
				);
		}

		return itemDAO.findAll();
		
	}

	private boolean saveToDB(Item item) {
		
		RetryTemplate template = retryConfig.createRetryTemplate();

		return template.execute(
			(context) -> { 
				boolean isSaved = false;
				Item dbItem = itemDAO.findOne(item.getId());
				// do we have enough items to sell?
				if (dbItem.getCount() >= item.getCount()) {
					dbItem.buy(item.getCount());
					itemDAO.save(dbItem);
					isSaved = true;
				}
				return isSaved;
			}
		);
	}
}
