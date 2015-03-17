package org.qdeve.example.angularjs.model;

import org.qdeve.example.angularjs.model.Item;

public class ItemUtils {
    public static Item anyItem() {
        return new Item.Builder().withCount(100).withName("anyItem").build();
    }
}
