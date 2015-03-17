package org.qdeve.example.angularjs.initializers;

import java.util.ArrayList;
import java.util.List;

import org.qdeve.example.angularjs.dao.ItemRepository;
import org.qdeve.example.angularjs.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * This Bean initialize the database with example data. By default it's 
 * enabled, however on production it can be turned off from 
 * application.properties by setting 'acme.initializers.db' to false.
 */
@Component
@ConditionalOnProperty(name="acme.initializers.db")
public class DbInitializator extends LifecycleBase {

	private static final Logger LOG = LoggerFactory.getLogger(DbInitializator.class);

	@Autowired
	private ItemRepository itemDAO;
	
	@Override
	public void start() {
		LOG.debug("Starting initialization of DB. ");
		List<Item> items = new ArrayList<>();
		items.add(new Item.Builder().withName("Item A").withCount(20).build());
		items.add(new Item.Builder().withName("Item B").withCount(10).build());
		items.add(new Item.Builder().withName("Item C").withCount(50).build());
		itemDAO.deleteAll();
		itemDAO.save(items);
		LOG.debug("Initialization completed.");
		super.start();
	}
}
