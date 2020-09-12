package tta.basics.daythree.one;

import java.lang.reflect.InvocationTargetException;

public class ShapeFactory {
	
	private static int ID = 0;
	
	public static Shape getShape(String name) {
		++ID;
		Shape shape = null;
		try {
			shape = (Shape)Class.forName(name).getConstructor(Integer.TYPE).newInstance(ID);
		} catch (InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return shape;
	}
}
