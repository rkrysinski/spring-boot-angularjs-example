package org.qdeve.example.angularjs.repo;

import java.util.List;

import org.qdeve.example.angularjs.data.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemManager {

	@Autowired
	private ItemRepository itemDAO;

	public List<Item> getAll() {
		return itemDAO.findAll();
	}
	
	//OptimisticLockException
	
	
}
