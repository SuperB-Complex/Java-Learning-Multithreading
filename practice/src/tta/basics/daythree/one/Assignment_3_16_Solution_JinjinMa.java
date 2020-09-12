package tta.basics.daythree.one;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class Assignment_3_16_Solution_JinjinMa {
		
	private static LinkedList<Shape> fill() {
		LinkedList<Shape> listOfShapes = new LinkedList<> ();
		Random seed = new Random();
		getShapes(listOfShapes, "tta.basics.daythree.one.Circle", "tta.basics.daythree.one.Rectangle", "tta.basics.daythree.one.Square");
		return listOfShapes;
	}
	
	private static void getShapes(LinkedList<Shape> listOfShapes, String... shapeName) {
		int length = shapeName.length;
		for (int i = 0; i < 18; i++) {
			Random seed = new Random();
			String name = shapeName[(i + 1) % length];
			Shape shape = ShapeFactory.getShape(name);
			switch(name) {
				case "tta.basics.daythree.one.Circle": shape.set(seed.nextFloat() * 10);
				   break;
				case "tta.basics.daythree.one.Rectangle": shape.set(seed.nextFloat() * 10, seed.nextFloat() * 10);
				   break;
				case "tta.basics.daythree.one.Square": shape.set(seed.nextFloat() * 10, seed.nextFloat() * 10);
				   break;
			}
			shape.calculateArea();
			listOfShapes.add(shape);
		}
	}
	
	public static void main(String[] args) {
		LinkedList<Shape> listOfShapes = fill();
		LinkedList<Shape> copyOflistOfShapes = (LinkedList)listOfShapes.clone();
		System.out.println("Before sorting based on area of the shape :");
		System.out.println(listOfShapes);
		Collections.sort(listOfShapes, new Comparator<Shape> () {
			@Override
			public int compare(Shape shape1, Shape shape2) {
				return shape1.compareTo(shape2);
			}
		});
		Collections.sort(copyOflistOfShapes);
		System.out.println("\nAfter sorting :");
		System.out.println(listOfShapes);
		System.out.println(copyOflistOfShapes);
	}
}
