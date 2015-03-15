package org.qdeve.example.angularjs.data;

import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Item {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	private String name;
	private int count;
	@Version
    private Integer version;

	public Item() {}
	
	public Item(String name, int count) {
		this.name = name;
		this.count = count;
	}

	public Item(String name, int count, long id) {
		this.name = name;
		this.count = count;
		this.id = id;
	}
	
	public Item(String name, int count, long id, Integer version) {
		this.name = name;
		this.count = count;
		this.id = id;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String item) {
		this.name = item;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void buy(int toBuy) {
		this.count -= toBuy;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Item)) {
			return false;
		}

		Item other = (Item) obj;

		if (name != null && !name.equals(other.name) 
				|| name == null && other.name != null) {
			return false;
		}

		if (count != other.count) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = 1;

		result = 31 * result + name.hashCode();
		result = 31 * result + Integer.hashCode(count);

		return result;
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, 
				"Item [name=%s, count=%d, id=%d, version=%d]",
				name, count, id, version);
	}

	public static class Builder {
		
		public static final int DEFAULT_COUNT_20 = 20;
		public static final String DEFAULT_NAME = "Ferrari";
		String name;
		int count;
		long id;
		Integer version;
		
		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withCount(int count) {
			this.count = count;
			return this;
		}

		public Item build() {
			return new Item(name, count, id, version);
		}

		public Builder withDefaultValues() {
			this.count = DEFAULT_COUNT_20;
			this.name  = DEFAULT_NAME;
			return this;
		}

		public Builder withId(long id) {
			this.id = id;
			return this;
		}
		
		public Builder withVersion(Integer version) {
			this.version = version;
			return this;
		}

		public Builder fromItem(Item item) {
			this.name    = item.getName();
			this.count   = item.getCount();
			this.id      = item.getId();
			this.version = item.getVersion();
			return this;
		}
	}
}
