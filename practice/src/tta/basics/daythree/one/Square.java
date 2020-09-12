package tta.basics.daythree.one;

public class Square implements Shape {

	private float width;
	private float area;
	private final int ID;

	public Square(int id) {
		this.ID = id;
	}

	public void set(float... parameters) {
		this.width = parameters[0];
		return;
	}
	
	@Override
	public float getArea() {
		return this.area;
	}
	
	@Override
	public float calculateArea() {
		this.area = this.width * this.width;
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
		return "Square" + getId() + "(" + String.valueOf(getArea()) + ")";
	}

}
