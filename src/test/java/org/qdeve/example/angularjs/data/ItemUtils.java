package org.qdeve.example.angularjs.data;

import org.qdeve.example.angularjs.data.Item;

public class ItemUtils {
    public static Item anyItem() {
        return new Item.Builder().withCount(100).withName("anyItem").build();
    }
}
