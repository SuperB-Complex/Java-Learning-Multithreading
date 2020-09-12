package tta.basics.daythree.one;

public class Circle implements Shape {

	private float radius;
	private float area;
	private final int ID;
	
	public Circle(int id) {
		this.ID = id;
	}
	
	@Override
	public void set(float... parameters) {
		this.radius = parameters[0];
		return;
	}
	
	@Override
	public float calculateArea() {
		this.area = Shape.PI * this.radius * this.radius;
		return this.area;
	}
	
	@Override
	public float getArea() {
		return this.area;
	}
	
	@Override
	public int getId() {
		return this.ID;
	}
	
	@Override
	public int compareTo(Shape shape) {
		return Float.compare(this.area, shape.getArea());
	}
	
	@Override
	public String toString() {
		return "Circle" + getId() + "(" + String.valueOf(getArea()) + ")";
	}

}
