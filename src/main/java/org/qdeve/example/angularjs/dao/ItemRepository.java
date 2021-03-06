package org.qdeve.example.angularjs.dao;

import java.util.List;

import javax.persistence.LockModeType;

import org.qdeve.example.angularjs.model.Item;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

	@Override
	@Lock(LockModeType.OPTIMISTIC)
	List<Item> findAll();
	
	@Override
	@Lock(LockModeType.OPTIMISTIC)
	<S extends Item> List<S> save(Iterable<S> items);
	
}
