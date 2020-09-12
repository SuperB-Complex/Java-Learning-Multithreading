package tta.basics.dayfive.one;

import java.util.List;

public class Student {
	
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
	public List<Laptop> getLaptops() {
		return laptops;
	}
	
	public void setLaptops(List<Laptop> laptops) {
		this.laptops = laptops;
	}
	
	public void student1(List<Laptop> laptops) {
		this.laptops = laptops;
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
		Student student = (Student)object;
		return student.getId() == this.getId();
	}

	private int id;
	private String name;
	private List<Laptop> laptops;
	
}
