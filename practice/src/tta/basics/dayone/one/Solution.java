package tta.basics.dayone.one;

import java.io.File;
import java.util.Scanner;

//from doSomething method we can make a reasonable assumption 
//the input should be an integer or a character 'q'
public class Solution {

	public static boolean isNumber(String input) {
		try {
			Long.parseLong(input);
		} catch(NumberFormatException e) {
			try {
				Double.parseDouble(input);
			} catch(NumberFormatException ee) {
				return false;
			}
			return true;
		}
		return true;
	}

	public static void doSomething(Integer input) {
		String name = input.toString() + ".txt";
		File file = new File(name);
		if (file.exists()) {
			System.out.println("File" + name + " already exists");
			return;
		}
		System.out.println("File" + name + " created successfully.");
		return;
	}

	public static void main(String[] args) {
		Scanner inputScanner = new Scanner(System.in);
		while (true) {
			System.out.println("Please input an integer or quit(q):");
			String input = inputScanner.nextLine();
			if (Solution.isNumber(input)) {
				Integer iinput = Integer.parseInt(input);
				if (iinput <= 0) {
					Solution.unQualified();
				} else {
					for (int i = 0; i < iinput; i++) {
						Solution.doSomething(i);
					}
				}
				// break;
				continue;
			} else {
				if (input.length() > 1 || input.length() == 0) {
					System.out.println("If you want to quit, please enter q to quit.");
					continue;
				} else if (input.equals("q")) {
					Solution.quit();
					break;
				}
			}
			return;
		}
	}

	public static void unQualified() {
		System.out.println("Your input is smaller or equal to 0, it is not allowed.");
	}

	public static void quit() {
		System.out.println("Bye bye.");
	}
}