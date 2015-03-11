package org.qdeve.example.angularjs.lifecycle;

import java.util.ArrayList;
import java.util.List;

import org.qdeve.example.angularjs.data.Item;
import org.qdeve.example.angularjs.repo.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DbInitializator extends LifecycleBase {

	private static final Logger LOG = LoggerFactory.getLogger(DbInitializator.class);

	@Autowired
	private ItemRepository itemDAO;
	
	@Value("${acme.lifecycle.dbinit}") 
	Boolean dbInit;

	@Override
	public void start() {
		
		if (dbInit) {
			LOG.debug("Starting initialization of DB. ");
			List<Item> items = new ArrayList<>();
			items.add(new Item.Builder().withName("Item A").withCount(20).build());
			items.add(new Item.Builder().withName("Item B").withCount(10).build());
			items.add(new Item.Builder().withName("Item C").withCount(50).build());
			itemDAO.save(items);
			LOG.debug("Initialization completed.");
		} else {
			LOG.debug("Skipping initialization of DB.");
		}

		super.start();
	}
}
