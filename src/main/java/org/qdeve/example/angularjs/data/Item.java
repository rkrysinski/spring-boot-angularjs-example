package org.qdeve.example.angularjs.data;

import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Item {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	private String name;
	private int count;

	public Item(String name, int count) {
		this.name = name;
		this.count = count;
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

		if (name != null && !name.endsWith(other.name)) {
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
				"Item [itemName=%s, count=%d]",
				name, count);
	}

	public static class Builder {
		String name;
		int count;

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withCount(int count) {
			this.count = count;
			return this;
		}

		public Item build() {
			return new Item(name, count);
		}
	}

}
