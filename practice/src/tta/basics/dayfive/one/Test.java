package tta.basics.dayfive.one;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Test {
	
	public static void main(String[] args) {
		
		Laptop laptop1 = new Laptop();
		laptop1.setId(1);
		laptop1.setName("laptop1");
		laptop1.setCapacity(1);
		Laptop laptop2 = new Laptop();
		laptop2.setId(1);
		laptop2.setName("laptop1");
		laptop2.setCapacity(1);
		
		List<Laptop> list = new LinkedList<> ();
		list.add(laptop1);
		list.add(laptop2);
		
		Student student1 = new Student();
		student1.setId(1);
		student1.setName("student1");
		student1.setLaptops(list);
		Student student2 = new Student();
		student2.setId(2);
		student2.setName("student2");
		student2.setLaptops(list);
		Student student3 = new Student();
		student3.setId(3);
		student3.setName("student3");
		student3.setLaptops(list);
		
		List<Student> students = new LinkedList<> ();
		students.add(student1);
		students.add(student2);
		students.add(student3);
		
		students
		.stream()
		.forEach((Student student) -> {
			List<Laptop> laptops = student.getLaptops();
			Integer sum = laptops
							.stream()
							.map(Laptop::getCapacity)
							.reduce(0, (a, b) -> {
								return a + b;
							});
			System.out.println(student.getName() + " has " + sum + " laptops");
		});
	}
}
