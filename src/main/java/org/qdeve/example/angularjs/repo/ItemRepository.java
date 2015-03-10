package org.qdeve.example.angularjs.repo;

import org.qdeve.example.angularjs.data.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

}
