package tta.basics.dayfive.one;

public class Laptop {
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		Laptop laptop = (Laptop)object;
		return laptop.getId() == this.getId();
	}
	
	private int id;
	private String name;
	private int capacity;
	
	
}
