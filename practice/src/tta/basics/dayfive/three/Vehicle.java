package tta.basics.dayfive.three;

public class Vehicle {
	
	private String plate;
	private String brand;
	private String type;
	private int direction;
	
	public Vehicle(String plate, String brand, String type) {
		this.plate = plate;
		this.brand = brand;
		this.type = type;
	}
	
	// two values
	// 0: left
	// 1: right
	public void setDirection(int direction) {
		this.direction = direction;
		return;
	}
}
