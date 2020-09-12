package tta.basics.daythree.one;

public interface Shape extends Comparable<Shape> {
	float PI = 3.1415926f;
	float calculateArea();
	float getArea();
	void set(float... parameters);
	int getId();
}
