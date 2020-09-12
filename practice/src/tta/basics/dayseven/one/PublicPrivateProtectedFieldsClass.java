package tta.basics.dayseven.one;

public class PublicPrivateProtectedFieldsClass {
	
	public PublicPrivateProtectedFieldsClass(int counter, String name, double income, long bigCounter, char letter,
			float water) {
		super();
		this.counter = counter;
		this.name = name;
		this.income = income;
		this.bigCounter = bigCounter;
		this.letter = letter;
		this.water = water;
	}
	
	private int counter;
	public String name;
	protected double income;
	private long bigCounter;
	public char letter;
	protected float water;
	
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public long getBigCounter() {
		return bigCounter;
	}
	public void setBigCounter(long bigCounter) {
		this.bigCounter = bigCounter;
	}
	public char getLetter() {
		return letter;
	}
	public void setLetter(char letter) {
		this.letter = letter;
	}
	public float getWater() {
		return water;
	}
	public void setWater(float water) {
		this.water = water;
	}

}
