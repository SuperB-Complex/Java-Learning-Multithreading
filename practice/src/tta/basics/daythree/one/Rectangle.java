package tta.basics.daythree.one;

public class Rectangle implements Shape {

	private float width;
	private float length;
	private float area;
	private final int ID;

	public Rectangle(int id) {
		this.ID = id;
	}
	
	public void set(float... parameters) {
		this.width = parameters[0];
		this.length = parameters[1];
		return;
	}
	
	@Override
	public float getArea() {
		return this.area;
	}
	
	@Override
	public float calculateArea() {
		this.area = this.width * this.length;
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
		return "Rectangle" + getId() + "(" + String.valueOf(getArea()) + ")";
	}

}
